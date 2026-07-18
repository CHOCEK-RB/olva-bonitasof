package com.olva.enviosapi.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
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
