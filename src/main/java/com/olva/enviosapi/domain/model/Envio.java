package com.olva.enviosapi.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "envios")
public class Envio {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "numero_tracking", nullable = false, unique = true)
  private String numeroTracking;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", nullable = false)
  private EstadoEnvio estado;

  @Column(name = "origen")
  private String origen;

  @Column(name = "destino")
  private String destino;

  @Column(name = "ubicacion_actual")
  private String ubicacionActual;

  @Column(name = "fecha_envio")
  private LocalDateTime fechaEnvio;

  @Column(name = "fecha_entrega_estimada")
  private LocalDateTime fechaEntregaEstimada;

  @Column(name = "lote_id")
  private String loteId;

  protected Envio() {
    // Constructor vacío requerido por JPA
  }

  public Envio(String numeroTracking, EstadoEnvio estado, String origen, String destino,
      String ubicacionActual, LocalDateTime fechaEnvio, LocalDateTime fechaEntregaEstimada) {
    this.numeroTracking = numeroTracking;
    this.estado = estado;
    this.origen = origen;
    this.destino = destino;
    this.ubicacionActual = ubicacionActual;
    this.fechaEnvio = fechaEnvio;
    this.fechaEntregaEstimada = fechaEntregaEstimada;
  }

  public Long getId() {
    return id;
  }

  public String getNumeroTracking() {
    return numeroTracking;
  }

  public EstadoEnvio getEstado() {
    return estado;
  }

  public String getOrigen() {
    return origen;
  }

  public String getDestino() {
    return destino;
  }

  public void setDestino(String destino) {
    this.destino = destino;
  }

  public String getUbicacionActual() {
    return ubicacionActual;
  }

  public LocalDateTime getFechaEnvio() {
    return fechaEnvio;
  }

  public LocalDateTime getFechaEntregaEstimada() {
    return fechaEntregaEstimada;
  }

  public void actualizarEstado(EstadoEnvio nuevoEstado, String nuevaUbicacion) {
    this.estado = nuevoEstado;
    this.ubicacionActual = nuevaUbicacion;
  }

  public String getLoteId() {
    return loteId;
  }

  public void setLoteId(String loteId) {
    this.loteId = loteId;
  }
}
