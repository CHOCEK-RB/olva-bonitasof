package com.olva.enviosapi.presentation.controller;

import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import com.olva.enviosapi.application.dto.EnvioTrackingResponse;
import com.olva.enviosapi.application.excepcion.EnvioNoEncontradoException;
import com.olva.enviosapi.application.service.IEnvioService;
import com.olva.enviosapi.application.dto.EnvioRequestDTO;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
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

  // GIVEN un envio id y DTO WHEN llamo a PUT /api/envios/{id}/recepcion THEN
  // actualiza pago y observaciones
  @PutMapping("/{id}/recepcion")
  public ResponseEntity<EnvioResponseDTO> recepcionEnvio(@PathVariable String id,
      @Valid @RequestBody com.olva.enviosapi.application.dto.RecepcionRequestDTO request) {
    EnvioResponseDTO response = envioService.recepcionEnvio(id, request);
    return ResponseEntity.ok(response);
  }

  // GIVEN un envio id WHEN llamo a POST /api/envios/{id}/despachar THEN publica evento en RabbitMQ
  @PostMapping("/{id}/despachar")
  public ResponseEntity<EnvioResponseDTO> despacharEnvio(@PathVariable String id) {
    EnvioResponseDTO response = envioService.despacharEnvio(id);
    return ResponseEntity.ok(response);
  }

  // GIVEN un envio id WHEN llamo a PUT /api/envios/{id}/pago THEN retorna
  // pagoConfirmado true
  @PutMapping("/{id}/pago")
  public ResponseEntity<EnvioResponseDTO> confirmarPago(@PathVariable String id) {
    EnvioResponseDTO response = envioService.confirmarPago(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/tracking/{tracking}")
  public ResponseEntity<EnvioTrackingResponse> consultarEstado(@PathVariable("tracking") String tracking) {
    EnvioTrackingResponse respuesta = envioService.consultarEstado(tracking);
    return ResponseEntity.ok(respuesta);
  }

  @PutMapping("/tracking/{tracking}/destino")
  public ResponseEntity<Void> actualizarDestino(@PathVariable("tracking") String tracking,
      @RequestBody com.olva.enviosapi.application.dto.DestinoRequest request) {
    envioService.actualizarDestino(tracking, request.getDireccionDestino());
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(EnvioNoEncontradoException.class)
  public ResponseEntity<String> manejarEnvioNoEncontrado(EnvioNoEncontradoException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }
}
