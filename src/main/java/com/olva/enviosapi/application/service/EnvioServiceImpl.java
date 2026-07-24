package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.EnvioRequestDTO;
import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import com.olva.enviosapi.application.dto.EnvioTrackingResponse;
import com.olva.enviosapi.application.excepcion.EnvioNoEncontradoException;
import com.olva.enviosapi.domain.model.EstadoEnvio;
import com.olva.enviosapi.domain.model.RegistroEnvio;
import com.olva.enviosapi.domain.repository.IRegistroEnvioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnvioServiceImpl implements IEnvioService {

  private final IRegistroEnvioRepository repository;
  private final org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

  @Override
  public List<EnvioResponseDTO> listarEnvios() {
    return repository.findAll().stream()
        .map(envio -> mapToDTO(envio, "Listado de envio"))
        .collect(Collectors.toList());
  }

  @Override
  public EnvioResponseDTO registrarEnvio(EnvioRequestDTO request) {
    double monto = request.getDatosPaquete().getPeso() * 5.0 +
        (request.getDatosPaquete().getValorDeclarado() != null ? request.getDatosPaquete().getValorDeclarado() * 0.01
            : 0);

    RegistroEnvio envio = RegistroEnvio.builder()
        .fechaRegistro(LocalDate.now())
        .tipoPago(
            request.getTipoPago() != null && !request.getTipoPago().isBlank() ? request.getTipoPago() : "Pendiente")
        .montoTotal(monto)
        .pagoConfirmado(false)
        .estado(EstadoEnvio.PENDIENTE)
        .remitente(request.getRemitente())
        .datosPaquete(request.getDatosPaquete())
        .build();

    envio = repository.save(envio);

    return mapToDTO(envio, "Envio registrado con exito. Pendiente de pago y rotulado.");
  }

  @Override
  public EnvioResponseDTO generarRotuloTracking(String id) {
    RegistroEnvio envio = repository.findById(id)
        .orElseThrow(() -> new EnvioNoEncontradoException(id));
    if (envio.getNumeroTracking() == null) {
      Random random = new Random();
      envio.setNumeroTracking("OLVA-" + (10000 + random.nextInt(90000)));
      envio.setComprobantePago("BOLETA-B" + (10000 + random.nextInt(90000)));
      envio.setEstado(EstadoEnvio.EN_TRANSITO);
      repository.save(envio);
    }

    return mapToDTO(envio, "Rotulo y tracking generados correctamente.");
  }

  @Override
  public EnvioResponseDTO confirmarPago(String id) {
    RegistroEnvio envio = repository.findById(id)
        .orElseThrow(() -> new EnvioNoEncontradoException(id));
    envio.setPagoConfirmado(true);
    repository.save(envio);

    return mapToDTO(envio, "Pago confirmado exitosamente.");
  }

  @Override
  public EnvioResponseDTO recepcionEnvio(String id, com.olva.enviosapi.application.dto.RecepcionRequestDTO request) {
    RegistroEnvio envio = repository.findById(id)
        .orElseThrow(() -> new EnvioNoEncontradoException(id));
    envio.setTipoPago(request.getTipoPago());
    envio.setObservacionesPaquete(request.getObservacionesPaquete());

    repository.save(envio);

    return mapToDTO(envio, "Recepcion completada. Datos actualizados en ventanilla.");
  }

  @Override
  public EnvioResponseDTO despacharEnvio(String id) {
    RegistroEnvio envio = repository.findById(id)
        .orElseThrow(() -> new EnvioNoEncontradoException(id));

    // Crear un Map para enviar como JSON
    java.util.Map<String, Object> mensajeAmqp = new java.util.HashMap<>();
    mensajeAmqp.put("envioId", envio.getId());
    mensajeAmqp.put("numeroTracking", envio.getNumeroTracking());
    mensajeAmqp.put("mensaje", "Paquete despachado y listo para clasificación");

    rabbitTemplate.convertAndSend(
        com.olva.enviosapi.application.config.RabbitMQConfig.EXCHANGE_NAME,
        com.olva.enviosapi.application.config.RabbitMQConfig.ROUTING_KEY,
        mensajeAmqp);

    return mapToDTO(envio, "Paquete despachado. Notificación enviada a RabbitMQ.");
  }

  @Override
  public EnvioTrackingResponse consultarEstado(String numeroTracking) {
    RegistroEnvio envio = repository.findByNumeroTracking(numeroTracking)
        .orElseThrow(() -> new EnvioNoEncontradoException(numeroTracking));

    return new EnvioTrackingResponse(
        envio.getId(),
        envio.getNumeroTracking(),
        envio.getEstado(),
        envio.getOrigen(),
        envio.getDestino(),
        envio.getUbicacionActual(),
        envio.getFechaEntregaEstimada());
  }

  @Override
  public void actualizarDestino(String numeroTracking, String nuevoDestino) {
    RegistroEnvio envio = repository.findByNumeroTracking(numeroTracking)
        .orElseThrow(() -> new EnvioNoEncontradoException(numeroTracking));
    envio.getDatosPaquete().setDireccionDestino(nuevoDestino);
    repository.save(envio);
  }

  private EnvioResponseDTO mapToDTO(RegistroEnvio envio, String mensaje) {
    return EnvioResponseDTO.builder()
        .id(envio.getId())
        .fechaRegistro(envio.getFechaRegistro())
        .estadoEnvio(envio.getEstado() != null ? envio.getEstado().name() : null)
        .montoTotal(envio.getMontoTotal())
        .pagoConfirmado(envio.getPagoConfirmado())
        .numeroTracking(envio.getNumeroTracking())
        .mensaje(mensaje)
        .build();
  }
}
