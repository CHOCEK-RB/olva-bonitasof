package com.olva.enviosapi.presentation.controller;

import com.olva.enviosapi.application.dto.ManifiestoInputDTO;
import com.olva.enviosapi.application.dto.ConfirmacionCargaDTO;
import com.olva.enviosapi.application.dto.DetalleNovedadDTO;
import com.olva.enviosapi.application.service.IDistribucionService;
import com.olva.enviosapi.domain.model.ManifiestoCarga;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manifiestos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DistribucionController {

  private final IDistribucionService distribucionService;

  // Dispara el evento: "Paquetes listos" desde Bonita Studio
  @PostMapping
  public ResponseEntity<Void> iniciarProceso(@Valid @RequestBody ManifiestoInputDTO inputDTO) {
    distribucionService.iniciarProcesoDistribucion(inputDTO);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  // Tarea Humana: "Cargar mercancía"
  @PutMapping("/{codigoManifiesto}/carga")
  public ResponseEntity<Void> registrarCarga(
      @PathVariable String codigoManifiesto,
      @RequestBody ConfirmacionCargaDTO confirmacionDTO) {

    distribucionService.registrarCargaMercancia(codigoManifiesto, confirmacionDTO);
    return ResponseEntity.ok().build();
  }

  // Tarea Humana: "Registrar recepción"
  @PutMapping("/{codigoManifiesto}/recepcion")
  public ResponseEntity<Void> registrarRecepcion(
      @PathVariable String codigoManifiesto,
      @RequestBody ConfirmacionCargaDTO confirmacionDTO) {

    distribucionService.registrarRecepcionMercancia(codigoManifiesto, confirmacionDTO);
    return ResponseEntity.ok().build();
  }

  // Tareas Excepcionales: "Reportar incidente en ruta" o "Registrar observación"
  @PostMapping("/{codigoManifiesto}/novedades")
  public ResponseEntity<Void> registrarNovedad(
      @PathVariable String codigoManifiesto,
      @Valid @RequestBody DetalleNovedadDTO novedadDTO) {

    distribucionService.registrarNovedad(codigoManifiesto, novedadDTO);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @PostMapping("/{codigoManifiesto}/recursos/solicitar")
  public ResponseEntity<Void> solicitarRecursos(@PathVariable String codigoManifiesto) {
    distribucionService.solicitarRecursosAsincrono(codigoManifiesto);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  // Endpoint de consulta: Obtener todos los manifiestos guardados
  @GetMapping
  public ResponseEntity<List<ManifiestoCarga>> listarManifiestos() {
    return ResponseEntity.ok(distribucionService.listarManifiestos());
  }

  // Endpoint de consulta: Obtener un manifiesto específico por su código de negocio
  @GetMapping("/{codigoManifiesto}")
  public ResponseEntity<ManifiestoCarga> obtenerManifiesto(@PathVariable String codigoManifiesto) {
    return ResponseEntity.ok(distribucionService.obtenerManifiesto(codigoManifiesto));
  }
}
