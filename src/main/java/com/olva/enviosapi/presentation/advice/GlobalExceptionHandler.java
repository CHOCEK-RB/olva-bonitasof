package com.olva.enviosapi.presentation.advice;

import com.olva.enviosapi.application.excepcion.EnvioNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EnvioNoEncontradoException.class)
  public ResponseEntity<Map<String, String>> manejarEnvioNoEncontrado(EnvioNoEncontradoException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", "Not Found");
    response.put("mensaje", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, String>> manejarRuntimeException(RuntimeException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", "Internal Server Error");
    response.put("mensaje", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
