package com.olva.customer_service.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Entidad de Dominio: Reclamo.
 *
 * Representa un reclamo de compensación generado a partir de un envío
 * perdido o dañado. Contiene la logica de negocio para calcular el
 * monto a devolver al cliente, siguiendo la regla establecida por OLVA:
 *
 * montoCompensacion = (valorDeclarado * 0.80) + flete
 *
 */
public class Reclamo {

    private Long id;
    private String numeroTracking;
    private BigDecimal valorDeclarado;
    private BigDecimal flete;
    private BigDecimal montoCompensacion;
    private EstadoReclamo estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // ──────────────────────────────────────────────────────────────────────
    // Constructores
    // ──────────────────────────────────────────────────────────────────────

    /** Constructor requerido para reconstrucción desde persistencia. */
    public Reclamo() {}

    /**
     * Constructor de fábrica para crear un nuevo reclamo y liquidar la
     * compensación en un solo paso
     *
     * @param numeroTracking número de tracking del envío afectado
     * @param valorDeclarado valor declarado del paquete en soles (PEN)
     * @param flete          costo del flete pagado por el cliente en soles (PEN)
     */
    public static Reclamo crear(String numeroTracking,
                                BigDecimal valorDeclarado,
                                BigDecimal flete) {
        Reclamo reclamo = new Reclamo();
        reclamo.numeroTracking = numeroTracking;
        reclamo.valorDeclarado = valorDeclarado;
        reclamo.flete = flete;
        reclamo.estado = EstadoReclamo.PENDIENTE;
        reclamo.fechaCreacion = LocalDateTime.now();
        reclamo.fechaActualizacion = LocalDateTime.now();
        reclamo.liquidarCompensacion();
        return reclamo;
    }

    // ──────────────────────────────────────────────────────────────────────
    // Lógica de negocio (Domain Services embebidos)
    // ──────────────────────────────────────────────────────────────────────

    /**
     * Calcula y asigna el monto de compensación siguiendo la regla de negocio:
     * <pre>montoCompensacion = (valorDeclarado * 0.80) + flete</pre>
     *
     * <p>El resultado se redondea a 2 decimales (HALF_UP) para cumplir con
     * estándares financieros.
     *
     * @throws IllegalStateException si valorDeclarado o flete son nulos
     */
    public void liquidarCompensacion() {
        if (valorDeclarado == null || flete == null) {
            throw new IllegalStateException(
                    "valorDeclarado y flete son requeridos para liquidar la compensación.");
        }
        BigDecimal porcentajeValor = valorDeclarado
                .multiply(new BigDecimal("0.80"))
                .setScale(2, RoundingMode.HALF_UP);

        this.montoCompensacion = porcentajeValor
                .add(flete)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Marca el reclamo como compensación ejecutada en el sistema.
     * Actualiza el estado y la fecha de última modificación.
     */
    public void marcarComoEjecutado() {
        this.estado = EstadoReclamo.EJECUTADO;
        this.fechaActualizacion = LocalDateTime.now();
    }

    // ──────────────────────────────────────────────────────────────────────
    // Getters y Setters
    // ──────────────────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroTracking() { return numeroTracking; }
    public void setNumeroTracking(String numeroTracking) { this.numeroTracking = numeroTracking; }

    public BigDecimal getValorDeclarado() { return valorDeclarado; }
    public void setValorDeclarado(BigDecimal valorDeclarado) { this.valorDeclarado = valorDeclarado; }

    public BigDecimal getFlete() { return flete; }
    public void setFlete(BigDecimal flete) { this.flete = flete; }

    public BigDecimal getMontoCompensacion() { return montoCompensacion; }
    public void setMontoCompensacion(BigDecimal montoCompensacion) { this.montoCompensacion = montoCompensacion; }

    public EstadoReclamo getEstado() { return estado; }
    public void setEstado(EstadoReclamo estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
