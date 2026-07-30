package com.olva.enviosapi.presentation.controller;

import com.olva.enviosapi.application.dto.DevolucionResponseDTO;
import com.olva.enviosapi.application.dto.IntentoFallidoRequestDTO;
import com.olva.enviosapi.application.dto.RecepcionAlmacenRequestDTO;
import com.olva.enviosapi.application.dto.ResolucionDevolucionRequestDTO;
import com.olva.enviosapi.application.service.IDevolucionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devoluciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DevolucionController {

  private final IDevolucionService devolucionService;

  @GetMapping
  public ResponseEntity<List<DevolucionResponseDTO>> listarDevoluciones() {
    return ResponseEntity.ok(devolucionService.listarDevoluciones());
  }

  @PostMapping("/intento-fallido")
  public ResponseEntity<DevolucionResponseDTO> registrarIntentoFallido(@Valid @RequestBody IntentoFallidoRequestDTO request) {
    DevolucionResponseDTO response = devolucionService.registrarIntentoFallido(request);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/recepcion-almacen")
  public ResponseEntity<DevolucionResponseDTO> recepcionarEnAlmacen(@Valid @RequestBody RecepcionAlmacenRequestDTO request) {
    DevolucionResponseDTO response = devolucionService.recepcionarEnAlmacen(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{tracking}")
  public ResponseEntity<DevolucionResponseDTO> consultarPorTracking(@PathVariable("tracking") String tracking) {
    DevolucionResponseDTO response = devolucionService.consultarPorTracking(tracking);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/resolucion")
  public ResponseEntity<DevolucionResponseDTO> registrarResolucion(@Valid @RequestBody ResolucionDevolucionRequestDTO request) {
    DevolucionResponseDTO response = devolucionService.registrarResolucion(request);
    return ResponseEntity.ok(response);
  }
}
