package com.olva.enviosapi.domain.model;

public enum EstadoEnvio {
  LISTO_PARA_DESPACHO("Listo para despacho"),
  EN_TRANSITO("En tránsito"),
  RECIBIDO_EN_ALMACEN("Recibido en almacén de destino"),
  FINALIZADO_EXITO("Carga disponible"),
  FINALIZADO_RETRASO("Retraso"),
  FINALIZADO_EMERGENCIA("Emergencia en ruta"),
  FINALIZADO_INCIDENCIA("Carga con incidencia");

  private final String descripcion;

  EstadoEnvio(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }
}
