package com.olva.enviosapi.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "lotes_despacho")
public class LoteDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String rutaDestino;
    private String estado;
    private LocalDateTime fechaCreacion;

    protected LoteDespacho() {}

    public LoteDespacho(String rutaDestino, String estado, LocalDateTime fechaCreacion) {
        this.rutaDestino = rutaDestino;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRutaDestino() {
        return rutaDestino;
    }

    public void setRutaDestino(String rutaDestino) {
        this.rutaDestino = rutaDestino;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
