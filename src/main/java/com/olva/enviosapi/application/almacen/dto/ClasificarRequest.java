package com.olva.enviosapi.application.almacen.dto;

public class ClasificarRequest {
    private String numeroTracking;

    public ClasificarRequest() {}

    public ClasificarRequest(String numeroTracking) {
        this.numeroTracking = numeroTracking;
    }

    public String getNumeroTracking() {
        return numeroTracking;
    }

    public void setNumeroTracking(String numeroTracking) {
        this.numeroTracking = numeroTracking;
    }
}
