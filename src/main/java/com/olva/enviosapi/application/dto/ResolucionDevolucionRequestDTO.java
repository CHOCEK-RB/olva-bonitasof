package com.olva.enviosapi.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para registrar la resolución final de devolución 
 * (reintento, devolución o custodia).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResolucionDevolucionRequestDTO {

  @NotBlank(message = "El numeroTracking es obligatorio")
  private String numeroTracking;

  @NotBlank(message = "La decision es obligatoria (REINTENTO, DEVOLUCION, CUSTODIA)")
  private String decision;

  private String observaciones;
}
