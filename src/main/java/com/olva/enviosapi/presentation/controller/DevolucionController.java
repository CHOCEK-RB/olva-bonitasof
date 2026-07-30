package com.olva.enviosapi.presentation.controller;

import com.olva.enviosapi.application.dto.DevolucionResponseDTO;
import com.olva.enviosapi.application.dto.IntentoFallidoRequestDTO;
import com.olva.enviosapi.application.dto.RecepcionAlmacenRequestDTO;
import com.olva.enviosapi.application.dto.ResolucionDevolucionRequestDTO;
import com.olva.enviosapi.application.service.IDevolucionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de devoluciones, recepciones en almacén y reenvíos.
 */
@RestController
@RequestMapping("/api/devoluciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DevolucionController {

  private final IDevolucionService devolucionService;

  /**
   * Lista todas las devoluciones registradas.
   *
   * @return Lista de DevolucionResponseDTO.
   */
  @GetMapping
  public ResponseEntity<List<DevolucionResponseDTO>> listarDevoluciones() {
    return ResponseEntity.ok(devolucionService.listarDevoluciones());
  }

  /**
   * Registra un intento fallido de entrega para un envío.
   *
   * @param request Datos del intento fallido.
   * @return DTO de la devolución creada.
   */
  @PostMapping("/intento-fallido")
  public ResponseEntity<DevolucionResponseDTO> registrarIntentoFallido(
      @Valid @RequestBody IntentoFallidoRequestDTO request) {
    DevolucionResponseDTO response = devolucionService.registrarIntentoFallido(request);
    return ResponseEntity.ok(response);
  }

  /**
   * Registra la recepción física del paquete en el almacén central.
   *
   * @param request Datos del ingreso a almacén.
   * @return DTO de la devolución actualizada.
   */
  @PutMapping("/recepcion-almacen")
  public ResponseEntity<DevolucionResponseDTO> recepsionarEnAlmacen(
      @Valid @RequestBody RecepcionAlmacenRequestDTO request) {
    DevolucionResponseDTO response = devolucionService.recepsionarEnAlmacen(request);
    return ResponseEntity.ok(response);
  }

  /**
   * Consulta el registro de devolución asociado a un número de tracking.
   *
   * @param tracking Número de seguimiento.
   * @return DTO con la devolución.
   */
  @GetMapping("/{tracking}")
  public ResponseEntity<DevolucionResponseDTO> consultarPorTracking(
      @PathVariable("tracking") String tracking) {
    DevolucionResponseDTO response = devolucionService.consultarPorTracking(tracking);
    return ResponseEntity.ok(response);
  }

  /**
   * Registra la resolución final tomada sobre la devolución.
   *
   * @param request Datos de la resolución.
   * @return DTO actualizado.
   */
  @PutMapping("/resolucion")
  public ResponseEntity<DevolucionResponseDTO> registrarResolucion(
      @Valid @RequestBody ResolucionDevolucionRequestDTO request) {
    DevolucionResponseDTO response = devolucionService.registrarResolucion(request);
    return ResponseEntity.ok(response);
  }
}
