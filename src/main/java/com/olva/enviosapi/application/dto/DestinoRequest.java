package com.olva.enviosapi.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para cambiar la dirección de destino de un paquete.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinoRequest {
  private String direccionDestino;
}
