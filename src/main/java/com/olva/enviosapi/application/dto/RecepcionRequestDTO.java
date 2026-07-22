package com.olva.enviosapi.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RecepcionRequestDTO {
  @NotBlank(message = "El tipo de pago es obligatorio en la recepcion")
  private String tipoPago;

  private String observacionesPaquete;
}
