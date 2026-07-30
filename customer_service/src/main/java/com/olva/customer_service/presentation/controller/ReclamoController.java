package com.olva.customer_service.presentation.controller;

import com.olva.customer_service.domain.model.Reclamo;
import com.olva.customer_service.domain.repository.ReclamoRepository;
import com.olva.customer_service.presentation.dto.ReclamoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reclamos")
@Tag(name = "Reclamos", description = "API de consulta del módulo de Gestión de Reclamos")
public class ReclamoController {

    private final ReclamoRepository reclamoRepository;

    public ReclamoController(ReclamoRepository reclamoRepository) {
        this.reclamoRepository = reclamoRepository;
    }

    // ──────────────────────────────────────────────────────────────────────
    // GET /api/v1/reclamos
    // ──────────────────────────────────────────────────────────────────────

    @GetMapping
    @Operation(
            summary = "Listar todos los reclamos",
            description = "Retorna la lista completa de reclamos registrados en el sistema."
    )
    @ApiResponse(responseCode = "200", description = "Lista de reclamos obtenida exitosamente")
    public ResponseEntity<List<ReclamoResponseDTO>> listarTodos() {
        List<ReclamoResponseDTO> reclamos = reclamoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reclamos);
    }

    // ──────────────────────────────────────────────────────────────────────
    // GET /api/v1/reclamos/{id}
    // ──────────────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar reclamo por ID",
            description = "Retorna el detalle de un reclamo específico por su ID interno."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reclamo encontrado",
                    content = @Content(schema = @Schema(implementation = ReclamoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reclamo no encontrado", content = @Content)
    })
    public ResponseEntity<ReclamoResponseDTO> buscarPorId(
            @Parameter(description = "ID interno del reclamo", required = true, example = "1")
            @PathVariable Long id) {
        return reclamoRepository.findById(id)
                .map(reclamo -> ResponseEntity.ok(toDTO(reclamo)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ──────────────────────────────────────────────────────────────────────
    // GET /api/v1/reclamos/tracking/{numeroTracking}
    // ──────────────────────────────────────────────────────────────────────

    @GetMapping("/tracking/{numeroTracking}")
    @Operation(
            summary = "Buscar reclamo por número de tracking",
            description = "Permite consultar el estado y compensación de un reclamo a partir del tracking del envío."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reclamo encontrado",
                    content = @Content(schema = @Schema(implementation = ReclamoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe reclamo para ese tracking", content = @Content)
    })
    public ResponseEntity<ReclamoResponseDTO> buscarPorTracking(
            @Parameter(description = "Número de tracking del envío", required = true, example = "OLV-2024-001234")
            @PathVariable String numeroTracking) {
        return reclamoRepository.findByNumeroTracking(numeroTracking)
                .map(reclamo -> ResponseEntity.ok(toDTO(reclamo)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ──────────────────────────────────────────────────────────────────────
    // Mapper
    // ──────────────────────────────────────────────────────────────────────

    private ReclamoResponseDTO toDTO(Reclamo reclamo) {
        return new ReclamoResponseDTO(
                reclamo.getId(),
                reclamo.getNumeroTracking(),
                reclamo.getValorDeclarado(),
                reclamo.getFlete(),
                reclamo.getMontoCompensacion(),
                reclamo.getEstado() != null ? reclamo.getEstado().name() : null,
                reclamo.getFechaCreacion(),
                reclamo.getFechaActualizacion()
        );
    }
}
