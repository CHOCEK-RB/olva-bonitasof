package com.olva.enviosapi.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleNovedadDTO {

  @NotBlank(message = "El detalle de la novedad no puede estar vacío")
  private String detalleNovedadInput;
}
