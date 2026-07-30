package com.olva.enviosapi.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecepcionAlmacenRequestDTO {

  @NotBlank(message = "El numeroTracking es obligatorio")
  private String numeroTracking;

  private String zonaAlmacen;
  private String observaciones;
}
