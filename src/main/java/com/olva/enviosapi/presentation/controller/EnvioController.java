package com.olva.enviosapi.presentation.controller;

import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import com.olva.enviosapi.application.service.IEnvioService;
import com.olva.enviosapi.application.dto.EnvioRequestDTO;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

  private final IEnvioService envioService;

  public EnvioController(IEnvioService envioService) {
    this.envioService = envioService;
  }

  // GIVEN una solicitud WHEN llamo a GET /api/envios THEN devuelve todos los
  // envios
  @GetMapping
  public ResponseEntity<List<EnvioResponseDTO>> listarEnvios() {
    return ResponseEntity.ok(envioService.listarEnvios());
  }

    // GIVEN un Payload de Envio WHEN llamo a POST /api/envios THEN devuelve estado
  // Recibido
  @PostMapping
  public ResponseEntity<EnvioResponseDTO> registrarEnvio(@Valid @RequestBody EnvioRequestDTO request) {
    EnvioResponseDTO response = envioService.registrarEnvio(request);
    return ResponseEntity.ok(response);
  }
  
  // GIVEN un envio id WHEN llamo a PUT /api/envios/{id}/generar-rotulo THEN
  // asigna OLVA-XXXX
  @PutMapping("/{id}/generar-rotulo")
  public ResponseEntity<EnvioResponseDTO> generarRotulo(@PathVariable String id) {
      EnvioResponseDTO response = envioService.generarRotuloTracking(id);
      return ResponseEntity.ok(response);
  }
  
  // GIVEN un envio id WHEN llamo a PUT /api/envios/{id}/pago THEN retorna pagoConfirmado true
  @PutMapping("/{id}/pago")
  public ResponseEntity<EnvioResponseDTO> confirmarPago(@PathVariable String id) {
      EnvioResponseDTO response = envioService.confirmarPago(id);
      return ResponseEntity.ok(response);
  }
}
