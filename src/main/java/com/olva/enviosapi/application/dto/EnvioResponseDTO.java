package com.olva.enviosapi.application.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta estándar que consolida el estado y datos clave de un envío registrado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvioResponseDTO {
  private String id;
  private LocalDate fechaRegistro;
  private String estadoEnvio;
  private Double montoTotal;
  private Boolean pagoConfirmado;
  private String numeroTracking;
  private String mensaje;
}
