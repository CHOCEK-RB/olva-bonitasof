package com.olva.enviosapi.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para registrar un intento fallido de entrega de un paquete.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntentoFallidoRequestDTO {

  @NotBlank(message = "El numeroTracking es obligatorio")
  private String numeroTracking;

  @NotBlank(message = "El motivo es obligatorio")
  private String motivo;

  private String observaciones;
}
