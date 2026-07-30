package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.DevolucionResponseDTO;
import com.olva.enviosapi.application.dto.IntentoFallidoRequestDTO;
import com.olva.enviosapi.application.dto.RecepcionAlmacenRequestDTO;
import com.olva.enviosapi.application.dto.ResolucionDevolucionRequestDTO;
import com.olva.enviosapi.application.excepcion.EnvioNoEncontradoException;
import com.olva.enviosapi.domain.model.DevolucionEnvio;
import com.olva.enviosapi.domain.model.EstadoEnvio;
import com.olva.enviosapi.domain.model.RegistroEnvio;
import com.olva.enviosapi.domain.repository.IDevolucionRepository;
import com.olva.enviosapi.domain.repository.IRegistroEnvioRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio para la gestión del flujo de devoluciones y reenvíos.
 */
@Service
@RequiredArgsConstructor
public class DevolucionServiceImpl implements IDevolucionService {

  private final IDevolucionRepository devolucionRepository;
  private final IRegistroEnvioRepository envioRepository;

  /**
   * Registra un intento fallido de entrega para un envío.
   *
   * @param request Datos del intento fallido.
   * @return DTO de la devolución registrada.
   */
  @Override
  public DevolucionResponseDTO registrarIntentoFallido(IntentoFallidoRequestDTO request) {
    RegistroEnvio envio = envioRepository.findByNumeroTracking(request.getNumeroTracking())
        .orElseThrow(() -> new EnvioNoEncontradoException(
            "No se encontró el envío con tracking: " + request.getNumeroTracking()));

    Optional<DevolucionEnvio> devExistente = devolucionRepository.findByNumeroTracking(
        request.getNumeroTracking());
    DevolucionEnvio devolucion;

    if (devExistente.isPresent()) {
      devolucion = devExistente.get();
      devolucion.setMotivo(request.getMotivo());
      devolucion.setObservaciones(request.getObservaciones());
      devolucion.setEstado("INTENTO_FALLIDO");
      devolucion.setFechaIntentoFallido(LocalDateTime.now());
    } else {
      devolucion = DevolucionEnvio.builder()
          .numeroTracking(request.getNumeroTracking())
          .motivo(request.getMotivo())
          .observaciones(request.getObservaciones())
          .estado("INTENTO_FALLIDO")
          .fechaIntentoFallido(LocalDateTime.now())
          .build();
    }

    envio.setEstado(EstadoEnvio.DEVUELTO);
    envioRepository.save(envio);

    devolucion = devolucionRepository.save(devolucion);
    return mapToDto(devolucion, envio, "Intento fallido registrado exitosamente.");
  }

  /**
   * Registra el ingreso físico del paquete devuelto al almacén central.
   *
   * @param request Datos de la recepción en almacén.
   * @return DTO de la devolución actualizada.
   */
  @Override
  public DevolucionResponseDTO recepsionarEnAlmacen(RecepcionAlmacenRequestDTO request) {
    DevolucionEnvio devolucion = devolucionRepository.findByNumeroTracking(
        request.getNumeroTracking())
        .orElseThrow(() -> new EnvioNoEncontradoException(
            "Paquete no registrado para devolución: " + request.getNumeroTracking()));

    devolucion.setEstado("EN_ALMACEN");
    boolean hasZona = request.getZonaAlmacen() != null && !request.getZonaAlmacen().isBlank();
    devolucion.setZonaAlmacen(hasZona ? request.getZonaAlmacen() : "ZONA-ESPERA-DEVOLUCION");
    if (request.getObservaciones() != null) {
      devolucion.setObservaciones(request.getObservaciones());
    }
    devolucion.setFechaRecepcionAlmacen(LocalDateTime.now());

    devolucion = devolucionRepository.save(devolucion);

    RegistroEnvio envio = envioRepository.findByNumeroTracking(request.getNumeroTracking())
        .orElseThrow(() -> new EnvioNoEncontradoException(request.getNumeroTracking()));

    return mapToDto(devolucion, envio, "Paquete recepcionado y verificado en almacén local.");
  }

  /**
   * Consulta el registro de devolución por su número de tracking.
   *
   * @param numeroTracking Número de seguimiento.
   * @return DTO con el detalle de la devolución.
   */
  @Override
  public DevolucionResponseDTO consultarPorTracking(String numeroTracking) {
    DevolucionEnvio devolucion = devolucionRepository.findByNumeroTracking(numeroTracking)
        .orElseThrow(() -> new EnvioNoEncontradoException(
            "No existe registro de devolución para el tracking: " + numeroTracking));

    RegistroEnvio envio = envioRepository.findByNumeroTracking(numeroTracking)
        .orElseThrow(() -> new EnvioNoEncontradoException(numeroTracking));

    return mapToDto(devolucion, envio, "Consulta de devolución obtenida con éxito.");
  }

  /**
   * Registra la resolución final sobre la devolución (reintento, devolución o custodia).
   *
   * @param request Datos de la resolución.
   * @return DTO actualizado.
   */
  @Override
  public DevolucionResponseDTO registrarResolucion(ResolucionDevolucionRequestDTO request) {
    DevolucionEnvio devolucion = devolucionRepository.findByNumeroTracking(
        request.getNumeroTracking())
        .orElseThrow(() -> new EnvioNoEncontradoException(
            "No se encontró proceso de devolución activo para tracking: "
                + request.getNumeroTracking()));

    RegistroEnvio envio = envioRepository.findByNumeroTracking(request.getNumeroTracking())
        .orElseThrow(() -> new EnvioNoEncontradoException(request.getNumeroTracking()));

    String dec = request.getDecision().toUpperCase();
    devolucion.setDecisionRemitente(dec);
    devolucion.setFechaResolucion(LocalDateTime.now());

    if (request.getObservaciones() != null) {
      devolucion.setObservaciones(request.getObservaciones());
    }

    switch (dec) {
      case "REINTENTO":
        devolucion.setEstado("REINTENTO_PROGRAMADO");
        envio.setEstado(EstadoEnvio.EN_REPARTO);
        break;
      case "DEVOLUCION":
        devolucion.setEstado("DEVUELTO_A_ORIGEN");
        envio.setEstado(EstadoEnvio.DEVUELTO);
        break;
      case "CUSTODIA":
        devolucion.setEstado("EN_CUSTODIA");
        break;
      default:
        devolucion.setEstado("RESOLUCION_PENDIENTE");
        break;
    }

    envioRepository.save(envio);
    devolucion = devolucionRepository.save(devolucion);

    return mapToDto(devolucion, envio, "Resolución de devolución registrada: " + dec);
  }

  /**
   * Lista todas las devoluciones registradas.
   *
   * @return Lista de DTOs de devoluciones.
   */
  @Override
  public List<DevolucionResponseDTO> listarDevoluciones() {
    return devolucionRepository.findAll().stream()
        .map(dev -> {
          RegistroEnvio envio = envioRepository.findByNumeroTracking(
              dev.getNumeroTracking()).orElse(null);
          return mapToDto(dev, envio, "Listado de devolución");
        })
        .collect(Collectors.toList());
  }

  private DevolucionResponseDTO mapToDto(
      DevolucionEnvio dev, RegistroEnvio envio, String mensaje) {
    String remitenteNombre = null;
    String remitenteCorreo = null;
    String remitenteTelefono = null;
    String origen = null;
    String destino = null;

    if (envio != null) {
      if (envio.getRemitente() != null) {
        remitenteNombre = envio.getRemitente().getNombres() + " "
            + envio.getRemitente().getApellidos();
        remitenteCorreo = envio.getRemitente().getCorreo();
        remitenteTelefono = envio.getRemitente().getTelefono();
      }
      origen = envio.getOrigen();
      destino = envio.getDestino();
    }

    return DevolucionResponseDTO.builder()
        .id(dev.getId())
        .numeroTracking(dev.getNumeroTracking())
        .motivo(dev.getMotivo())
        .observaciones(dev.getObservaciones())
        .estado(dev.getEstado())
        .zonaAlmacen(dev.getZonaAlmacen())
        .decisionRemitente(dev.getDecisionRemitente())
        .fechaIntentoFallido(dev.getFechaIntentoFallido())
        .fechaRecepcionAlmacen(dev.getFechaRecepcionAlmacen())
        .fechaResolucion(dev.getFechaResolucion())
        .remitenteNombre(remitenteNombre)
        .remitenteCorreo(remitenteCorreo)
        .remitenteTelefono(remitenteTelefono)
        .direccionOrigen(origen)
        .direccionDestino(destino)
        .mensaje(mensaje)
        .build();
  }
}
