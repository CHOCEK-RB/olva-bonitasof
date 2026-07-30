package com.olva.customer_service.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para la API REST de consulta de reclamos.
 *
 * @param id                 identificador interno del reclamo
 * @param numeroTracking     número de tracking del envío
 * @param valorDeclarado     valor declarado original del paquete (PEN)
 * @param flete              flete pagado por el cliente (PEN)
 * @param montoCompensacion  monto calculado a devolver: (valorDeclarado*0.80)+flete (PEN)
 * @param estado             estado actual del reclamo (PENDIENTE/EJECUTADO/RECHAZADO)
 * @param fechaCreacion      timestamp de creación del reclamo
 * @param fechaActualizacion timestamp de última actualización
 */
public record ReclamoResponseDTO(
        Long id,
        String numeroTracking,
        BigDecimal valorDeclarado,
        BigDecimal flete,
        BigDecimal montoCompensacion,
        String estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {}
