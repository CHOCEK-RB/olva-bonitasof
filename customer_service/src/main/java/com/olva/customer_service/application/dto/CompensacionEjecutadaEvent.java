package com.olva.customer_service.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de salida: evento que el microservicio publica en la cola
 * {@code reclamos.compensacion.respuesta.queue} para confirmar al BPM
 * que la compensación fue ejecutada exitosamente en el sistema.
 *
 * @param numeroTracking     tracking del envío compensado
 * @param montoCompensacion  monto calculado y pagado
 * @param estado             resultado del proceso ("EJECUTADO" o "RECHAZADO")
 * @param mensaje            descripción legible del resultado
 * @param fechaProcesamiento timestamp de ejecución
 */
public record CompensacionEjecutadaEvent(
        String numeroTracking,
        BigDecimal montoCompensacion,
        String estado,
        String mensaje,
        LocalDateTime fechaProcesamiento
) {}
