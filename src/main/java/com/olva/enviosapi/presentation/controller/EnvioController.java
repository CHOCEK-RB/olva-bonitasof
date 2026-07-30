package com.olva.enviosapi.presentation.controller;

import com.olva.enviosapi.application.dto.DestinoRequest;
import com.olva.enviosapi.application.dto.EnvioRequestDTO;
import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import com.olva.enviosapi.application.dto.EnvioTrackingResponse;
import com.olva.enviosapi.application.dto.RecepcionRequestDTO;
import com.olva.enviosapi.application.service.IEnvioService;
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
 * Controlador REST para gestionar operaciones relacionadas con envios.
 */
@RestController
@RequestMapping("/api/envios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EnvioController {

  private final IEnvioService envioService;

  // GIVEN una solicitud WHEN llamo a GET /api/envios THEN devuelve todos los
  // envios
  /**
   * Metodo de ejecucion o procesamiento.
   */
  @GetMapping
  public ResponseEntity<List<EnvioResponseDTO>> listarEnvios() {
    return ResponseEntity.ok(envioService.listarEnvios());
  }

  // GIVEN un Payload de Envio WHEN llamo a POST /api/envios THEN devuelve estado
  // Recibido
  @PostMapping
  public ResponseEntity<EnvioResponseDTO> registrarEnvio(
      @Valid @RequestBody EnvioRequestDTO request) {
    EnvioResponseDTO response = envioService.registrarEnvio(request);
    return ResponseEntity.ok(response);
  }

  // GIVEN un envio id WHEN llamo a PUT /api/envios/{id}/generar-rotulo THEN
  // asigna OLVA-XXXX
  /**
   * Metodo de ejecucion o procesamiento.
   */
  @PutMapping("/{id}/generar-rotulo")
  public ResponseEntity<EnvioResponseDTO> generarRotulo(@PathVariable String id) {
    EnvioResponseDTO response = envioService.generarRotuloTracking(id);
    return ResponseEntity.ok(response);
  }

  // GIVEN un envio id y DTO WHEN llamo a PUT /api/envios/{id}/recepcion THEN
  // actualiza pago y observaciones
  @PutMapping("/{id}/recepcion")
  public ResponseEntity<EnvioResponseDTO> recepcionEnvio(@PathVariable String id,
      @Valid @RequestBody RecepcionRequestDTO request) {
    EnvioResponseDTO response = envioService.recepcionEnvio(id, request);
    return ResponseEntity.ok(response);
  }

  // GIVEN un envio id WHEN llamo a POST /api/envios/{id}/despachar THEN publica
  // evento en RabbitMQ
  /**
   * Metodo de ejecucion o procesamiento.
   */
  @PostMapping("/{id}/despachar")
  public ResponseEntity<EnvioResponseDTO> despacharEnvio(@PathVariable String id) {
    EnvioResponseDTO response = envioService.despacharEnvio(id);
    return ResponseEntity.ok(response);
  }

  // GIVEN un envio id WHEN llamo a PUT /api/envios/{id}/pago THEN retorna
  // pagoConfirmado true
  /**
   * Metodo de ejecucion o procesamiento.
   */
  @PutMapping("/{id}/pago")
  public ResponseEntity<EnvioResponseDTO> confirmarPago(@PathVariable String id) {
    EnvioResponseDTO response = envioService.confirmarPago(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/tracking/{tracking}")
  public ResponseEntity<EnvioTrackingResponse> consultarEstado(
      @PathVariable("tracking") String tracking) {
    EnvioTrackingResponse respuesta = envioService.consultarEstado(tracking);
    return ResponseEntity.ok(respuesta);
  }

  /**
   * Metodo de ejecucion o procesamiento.
   */
  @PutMapping("/tracking/{tracking}/destino")
  public ResponseEntity<Void> actualizarDestino(@PathVariable("tracking") String tracking,
      @RequestBody DestinoRequest request) {
    envioService.actualizarDestino(tracking, request.getDireccionDestino());
    return ResponseEntity.ok().build();
  }

}
