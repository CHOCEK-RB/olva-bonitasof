package com.olva.enviosapi.application.dto;

public class DestinoRequest {
  private String direccionDestino;

  public DestinoRequest() {
  }

  public DestinoRequest(String direccionDestino) {
    this.direccionDestino = direccionDestino;
  }

  public String getDireccionDestino() {
    return direccionDestino;
  }

  public void setDireccionDestino(String direccionDestino) {
    this.direccionDestino = direccionDestino;
  }
}
