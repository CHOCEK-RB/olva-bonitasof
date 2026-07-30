package com.olva.enviosapi.presentation.advice;

import com.olva.enviosapi.application.excepcion.EnvioNoEncontradoException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para la API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Maneja excepciones de tipo EnvioNoEncontradoException.
   *
   * @param ex La excepcion lanzada.
   * @return Respuesta HTTP 404 con detalles.
   */
  @ExceptionHandler(EnvioNoEncontradoException.class)
  public ResponseEntity<Map<String, String>> manejarEnvioNoEncontrado(
      EnvioNoEncontradoException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", "Not Found");
    response.put("mensaje", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  /**
   * Maneja excepciones generales no controladas.
   *
   * @param ex La excepcion lanzada.
   * @return Respuesta HTTP 500 con detalles.
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, String>> manejarRuntimeException(RuntimeException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", "Internal Server Error");
    response.put("mensaje", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
