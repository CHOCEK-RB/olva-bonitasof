package com.olva.enviosapi.domain.model;

public class Novedad extends RuntimeException {

  private final String codigoManifiesto;
  private final String detalleNovedad;

  public Novedad(String codigoManifiesto, String detalleNovedad) {
    super(String.format("Novedad en manifiesto %s: %s", codigoManifiesto, detalleNovedad));
    this.codigoManifiesto = codigoManifiesto;
    this.detalleNovedad = detalleNovedad;
  }

  public String getCodigoManifiesto() {
    return codigoManifiesto;
  }

  public String getDetalleNovedad() {
    return detalleNovedad;
  }
}
