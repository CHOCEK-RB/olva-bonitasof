package com.olva.enviosapi.presentation.controller;

import com.olva.enviosapi.application.dto.ClasificarRequest;
import com.olva.enviosapi.application.service.AlmacenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/almacen")
@RequiredArgsConstructor
public class AlmacenController {

  private final AlmacenService almacenService;

  @PostMapping("/clasificar")
  public ResponseEntity<Map<String, String>> clasificar(@RequestBody ClasificarRequest request) {
    String loteId = almacenService.clasificarEnvio(request.getNumeroTracking());

    Map<String, String> response = new HashMap<>();
    response.put("loteId", loteId);

    return ResponseEntity.ok(response);
  }
}
