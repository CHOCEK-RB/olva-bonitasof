package com.olva.enviosapi.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar la información recopilada durante la recepción física del paquete en ventanilla.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecepcionRequestDTO {
  private String tipoPago;
  private String observacionesPaquete;
}
