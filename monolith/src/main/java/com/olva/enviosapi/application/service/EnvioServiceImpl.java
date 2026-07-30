package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.config.RabbitMQConfig;
import com.olva.enviosapi.application.dto.EnvioRequestDTO;
import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import com.olva.enviosapi.application.dto.EnvioTrackingResponse;
import com.olva.enviosapi.application.dto.RecepcionRequestDTO;
import com.olva.enviosapi.application.excepcion.EnvioNoEncontradoException;
import com.olva.enviosapi.domain.model.EstadoEnvio;
import com.olva.enviosapi.domain.model.RegistroEnvio;
import com.olva.enviosapi.domain.repository.IRegistroEnvioRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio para la gestión del flujo completo de envíos.
 */
@Service
@RequiredArgsConstructor
public class EnvioServiceImpl implements IEnvioService {

  private final IRegistroEnvioRepository repository;
  private final org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

  /**
   * Lista todos los envíos almacenados en la base de datos.
   *
   * @return Lista de EnvioResponseDTO.
   */
  @Override
  public List<EnvioResponseDTO> listarEnvios() {
    return repository.findAll().stream().map(envio -> mapToDto(envio, "Listado de envio"))
        .collect(Collectors.toList());
  }

  /**
   * Registra un nuevo envío calculando la tarifa según peso y valor declarado.
   *
   * @param request Datos del envío.
   * @return DTO del envío registrado.
   */
  @Override
  public EnvioResponseDTO registrarEnvio(EnvioRequestDTO request) {
    double valorDeclarado = request.getDatosPaquete().getValorDeclarado() != null
        ? request.getDatosPaquete().getValorDeclarado() * 0.01
        : 0;
    double monto = request.getDatosPaquete().getPeso() * 5.0 + valorDeclarado;

    RegistroEnvio envio = RegistroEnvio.builder().fechaRegistro(LocalDate.now())
        .tipoPago(request.getTipoPago() != null && !request.getTipoPago().isBlank()
            ? request.getTipoPago()
            : "Pendiente")
        .montoTotal(monto).pagoConfirmado(false).estado(EstadoEnvio.PENDIENTE)
        .remitente(request.getRemitente()).datosPaquete(request.getDatosPaquete()).build();

    envio = repository.save(envio);

    return mapToDto(envio, "Envio registrado con exito. Pendiente de pago y rotulado.");
  }

  /**
   * Genera el código de tracking y el número de boleta para el envío especificado.
   *
   * @param id ID del envío.
   * @return DTO del envío rotulado.
   */
  @Override
  public EnvioResponseDTO generarRotuloTracking(String id) {
    RegistroEnvio envio =
        repository.findById(id).orElseThrow(() -> new EnvioNoEncontradoException(id));
    if (envio.getNumeroTracking() == null) {
      Random random = new Random();
      envio.setNumeroTracking("OLVA-" + (10000 + random.nextInt(90000)));
      envio.setComprobantePago("BOLETA-B" + (10000 + random.nextInt(90000)));
      envio.setEstado(EstadoEnvio.EN_TRANSITO);
      repository.save(envio);
    }

    return mapToDto(envio, "Rotulo y tracking generados correctamente.");
  }

  /**
   * Confirma el pago de una orden de envío.
   *
   * @param id ID del envío.
   * @return DTO actualizado.
   */
  @Override
  public EnvioResponseDTO confirmarPago(String id) {
    RegistroEnvio envio =
        repository.findById(id).orElseThrow(() -> new EnvioNoEncontradoException(id));
    envio.setPagoConfirmado(true);
    repository.save(envio);

    return mapToDto(envio, "Pago confirmado exitosamente.");
  }

  /**
   * Registra las observaciones y la modalidad de pago en la ventanilla.
   *
   * @param id ID del envío.
   * @param request Datos de la recepción.
   * @return DTO actualizado.
   */
  @Override
  public EnvioResponseDTO recepcionEnvio(String id, RecepcionRequestDTO request) {
    RegistroEnvio envio =
        repository.findById(id).orElseThrow(() -> new EnvioNoEncontradoException(id));
    envio.setTipoPago(request.getTipoPago());
    envio.setObservacionesPaquete(request.getObservacionesPaquete());

    repository.save(envio);

    return mapToDto(envio, "Recepcion completada. Datos actualizados en ventanilla.");
  }

  /**
   * Envía un mensaje AMQP a RabbitMQ notificando que el paquete fue despachado.
   *
   * @param id ID del envío.
   * @return DTO de respuesta.
   */
  @Override
  public EnvioResponseDTO despacharEnvio(String id) {
    RegistroEnvio envio =
        repository.findById(id).orElseThrow(() -> new EnvioNoEncontradoException(id));

    java.util.Map<String, Object> mensajeAmqp = new java.util.HashMap<>();
    mensajeAmqp.put("envioId", envio.getId());
    mensajeAmqp.put("numeroTracking", envio.getNumeroTracking());
    mensajeAmqp.put("mensaje", "Paquete despachado y listo para clasificación");

    rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY,
        mensajeAmqp);

    return mapToDto(envio, "Paquete despachado. Notificación enviada a RabbitMQ.");
  }

  /**
   * Obtiene la ubicación y estado actual de un paquete dado su número de tracking.
   *
   * @param numeroTracking Número de seguimiento.
   * @return DTO con datos de seguimiento.
   */
  @Override
  public EnvioTrackingResponse consultarEstado(String numeroTracking) {
    RegistroEnvio envio = repository.findByNumeroTracking(numeroTracking)
        .orElseThrow(() -> new EnvioNoEncontradoException(numeroTracking));

    return new EnvioTrackingResponse(envio.getId(), envio.getNumeroTracking(), envio.getEstado(),
        envio.getOrigen(), envio.getDestino(), envio.getUbicacionActual(),
        envio.getFechaEntregaEstimada());
  }

  /**
   * Modifica la dirección de destino del paquete asociado al número de tracking.
   *
   * @param numeroTracking Código tracking del paquete.
   * @param nuevoDestino Nueva dirección de destino.
   */
  @Override
  public void actualizarDestino(String numeroTracking, String nuevoDestino) {
    RegistroEnvio envio = repository.findByNumeroTracking(numeroTracking)
        .orElseThrow(() -> new EnvioNoEncontradoException(numeroTracking));
    envio.getDatosPaquete().setDireccionDestino(nuevoDestino);
    repository.save(envio);
  }

  private EnvioResponseDTO mapToDto(RegistroEnvio envio, String mensaje) {
    return EnvioResponseDTO.builder().id(envio.getId()).fechaRegistro(envio.getFechaRegistro())
        .estadoEnvio(envio.getEstado() != null ? envio.getEstado().name() : null)
        .montoTotal(envio.getMontoTotal()).pagoConfirmado(envio.getPagoConfirmado())
        .numeroTracking(envio.getNumeroTracking()).mensaje(mensaje).build();
  }
}
