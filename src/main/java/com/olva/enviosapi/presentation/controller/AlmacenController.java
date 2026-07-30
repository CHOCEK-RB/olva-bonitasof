package com.olva.enviosapi.presentation.controller;

import com.olva.enviosapi.application.dto.ClasificarRequest;
import com.olva.enviosapi.application.service.AlmacenService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operaciones del almacen y clasificacion.
 */
@RestController
@RequestMapping("/api/almacen")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AlmacenController {

  private final AlmacenService almacenService;

  /**
   * Clasifica un envio y le asigna un lote de despacho.
   *
   * @param request Datos del envio a clasificar.
   * @return Lote ID asignado.
   */
  @PostMapping("/clasificar")
  public ResponseEntity<Map<String, String>> clasificar(@RequestBody ClasificarRequest request) {
    String loteId = almacenService.clasificarEnvio(request.getNumeroTracking());

    Map<String, String> response = new HashMap<>();
    response.put("loteId", loteId);

    return ResponseEntity.ok(response);
  }
}
