package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.EnvioRequestDTO;
import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import com.olva.enviosapi.application.dto.EnvioTrackingResponse;
import com.olva.enviosapi.application.excepcion.EnvioNoEncontradoException;
import com.olva.enviosapi.domain.model.Envio;
import com.olva.enviosapi.domain.model.RegistroEnvio;
import com.olva.enviosapi.domain.repository.IEnvioRepository;
import com.olva.enviosapi.domain.repository.IRegistroEnvioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class EnvioServiceImpl implements IEnvioService {

  private final IRegistroEnvioRepository repository;
  private final IEnvioRepository envioRepository;

  public EnvioServiceImpl(IRegistroEnvioRepository repository,
                          IEnvioRepository envioRepository) {
    this.repository = repository;
    this.envioRepository = envioRepository;
  }

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
        .tipoPago(request.getTipoPago())
        .montoTotal(monto)
        .pagoConfirmado(false)
        .estadoEnvio("Recibido")
        .remitente(request.getRemitente())
        .datosPaquete(request.getDatosPaquete())
        .build();

    envio = repository.save(envio);

    return mapToDTO(envio, "Envio registrado con exito. Pendiente de pago y rotulado.");
  }

  @Override
  public EnvioResponseDTO generarRotuloTracking(String id) {
    Optional<RegistroEnvio> envioOpt = repository.findById(id);
    if (envioOpt.isEmpty()) {
      return EnvioResponseDTO.builder().mensaje("ID de envio no encontrado").build();
    }

    RegistroEnvio envio = envioOpt.get();
    if (envio.getNumeroTracking() == null) {
      Random random = new Random();
      envio.setNumeroTracking("OLVA-" + (10000 + random.nextInt(90000)));
      envio.setComprobantePago("BOLETA-B" + (10000 + random.nextInt(90000)));
      envio.setEstadoEnvio("En red");
      repository.save(envio);
    }

    return mapToDTO(envio, "Rotulo y tracking generados correctamente.");
  }

  @Override
  public EnvioResponseDTO confirmarPago(String id) {
    Optional<RegistroEnvio> envioOpt = repository.findById(id);
    if (envioOpt.isEmpty()) {
      return EnvioResponseDTO.builder().mensaje("ID de envio no encontrado").build();
    }

    RegistroEnvio envio = envioOpt.get();
    envio.setPagoConfirmado(true);
    repository.save(envio);

    return mapToDTO(envio, "Pago confirmado exitosamente.");
  }

  @Override
  public EnvioTrackingResponse consultarEstado(String numeroTracking) {
    Envio envio = envioRepository.buscarPorNumeroTracking(numeroTracking)
            .orElseThrow(() -> new EnvioNoEncontradoException(numeroTracking));

    return new EnvioTrackingResponse(
            envio.getNumeroTracking(),
            envio.getEstado(),
            envio.getOrigen(),
            envio.getDestino(),
            envio.getUbicacionActual(),
            envio.getFechaEntregaEstimada()
    );
  }

  private EnvioResponseDTO mapToDTO(RegistroEnvio envio, String mensaje) {
    return EnvioResponseDTO.builder()
        .id(envio.getId())
        .fechaRegistro(envio.getFechaRegistro())
        .estadoEnvio(envio.getEstadoEnvio())
        .montoTotal(envio.getMontoTotal())
        .pagoConfirmado(envio.getPagoConfirmado())
        .numeroTracking(envio.getNumeroTracking())
        .mensaje(mensaje)
        .build();
  }
}
