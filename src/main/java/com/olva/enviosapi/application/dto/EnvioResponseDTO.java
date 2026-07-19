package com.olva.enviosapi.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvioResponseDTO {
  private String id;
  private String numeroTracking;
  private String estadoEnvio;
  private Double montoTotal;
  private String comprobantePago;
  private Boolean pagoConfirmado;
  private LocalDate fechaRegistro;
  private String mensaje;
}
