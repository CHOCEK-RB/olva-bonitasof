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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DevolucionServiceImpl implements IDevolucionService {

  private final IDevolucionRepository devolucionRepository;
  private final IRegistroEnvioRepository envioRepository;

  @Override
  public DevolucionResponseDTO registrarIntentoFallido(IntentoFallidoRequestDTO request) {
    RegistroEnvio envio = envioRepository.findByNumeroTracking(request.getNumeroTracking())
        .orElseThrow(() -> new EnvioNoEncontradoException("No se encontró el envío con tracking: " + request.getNumeroTracking()));

    Optional<DevolucionEnvio> devExistente = devolucionRepository.findByNumeroTracking(request.getNumeroTracking());
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
    return mapToDTO(devolucion, envio, "Intento fallido registrado exitosamente.");
  }

  @Override
  public DevolucionResponseDTO recepcionarEnAlmacen(RecepcionAlmacenRequestDTO request) {
    DevolucionEnvio devolucion = devolucionRepository.findByNumeroTracking(request.getNumeroTracking())
        .orElseThrow(() -> new RuntimeException("Paquete incorrecto o no registrado para devolución: " + request.getNumeroTracking()));

    RegistroEnvio envio = envioRepository.findByNumeroTracking(request.getNumeroTracking())
        .orElseThrow(() -> new EnvioNoEncontradoException(request.getNumeroTracking()));

    devolucion.setEstado("EN_ALMACEN");
    devolucion.setZonaAlmacen(request.getZonaAlmacen() != null && !request.getZonaAlmacen().isBlank() 
        ? request.getZonaAlmacen() : "ZONA-ESPERA-DEVOLUCION");
    if (request.getObservaciones() != null) {
      devolucion.setObservaciones(request.getObservaciones());
    }
    devolucion.setFechaRecepcionAlmacen(LocalDateTime.now());

    devolucion = devolucionRepository.save(devolucion);
    return mapToDTO(devolucion, envio, "Paquete recepcionado y verificado en almacén local.");
  }

  @Override
  public DevolucionResponseDTO consultarPorTracking(String numeroTracking) {
    DevolucionEnvio devolucion = devolucionRepository.findByNumeroTracking(numeroTracking)
        .orElseThrow(() -> new RuntimeException("No existe registro de devolución para el tracking: " + numeroTracking));

    RegistroEnvio envio = envioRepository.findByNumeroTracking(numeroTracking)
        .orElseThrow(() -> new EnvioNoEncontradoException(numeroTracking));

    return mapToDTO(devolucion, envio, "Consulta de devolución obtenida con éxito.");
  }

  @Override
  public DevolucionResponseDTO registrarResolucion(ResolucionDevolucionRequestDTO request) {
    DevolucionEnvio devolucion = devolucionRepository.findByNumeroTracking(request.getNumeroTracking())
        .orElseThrow(() -> new RuntimeException("No se encontró proceso de devolución activo para tracking: " + request.getNumeroTracking()));

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

    return mapToDTO(devolucion, envio, "Resolución de devolución registrada: " + dec);
  }

  @Override
  public List<DevolucionResponseDTO> listarDevoluciones() {
    return devolucionRepository.findAll().stream()
        .map(dev -> {
          RegistroEnvio envio = envioRepository.findByNumeroTracking(dev.getNumeroTracking()).orElse(null);
          return mapToDTO(dev, envio, "Listado de devolución");
        })
        .collect(Collectors.toList());
  }

  private DevolucionResponseDTO mapToDTO(DevolucionEnvio dev, RegistroEnvio envio, String mensaje) {
    String remitenteNombre = null;
    String remitenteCorreo = null;
    String remitenteTelefono = null;
    String origen = null;
    String destino = null;

    if (envio != null) {
      if (envio.getRemitente() != null) {
        remitenteNombre = envio.getRemitente().getNombres() + " " + envio.getRemitente().getApellidos();
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
