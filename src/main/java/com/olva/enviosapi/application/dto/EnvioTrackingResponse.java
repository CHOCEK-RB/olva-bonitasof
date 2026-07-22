package com.olva.enviosapi.application.dto;

import com.olva.enviosapi.domain.model.EstadoEnvio;

import java.time.LocalDateTime;

public class EnvioTrackingResponse {

    private final String numeroTracking;
    private final EstadoEnvio estado;
    private final String origen;
    private final String destino;
    private final String ubicacionActual;
    private final LocalDateTime fechaEntregaEstimada;

    public EnvioTrackingResponse(String numeroTracking, EstadoEnvio estado, String origen,
                                 String destino, String ubicacionActual,
                                 LocalDateTime fechaEntregaEstimada) {
        this.numeroTracking = numeroTracking;
        this.estado = estado;
        this.origen = origen;
        this.destino = destino;
        this.ubicacionActual = ubicacionActual;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public String getNumeroTracking() { return numeroTracking; }
    public EstadoEnvio getEstado() { return estado; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public String getUbicacionActual() { return ubicacionActual; }
    public LocalDateTime getFechaEntregaEstimada() { return fechaEntregaEstimada; }
}