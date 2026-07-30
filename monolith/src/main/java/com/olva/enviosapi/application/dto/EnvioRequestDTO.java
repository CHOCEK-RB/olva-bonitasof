package com.olva.enviosapi.application.dto;

import com.olva.enviosapi.domain.model.Cliente;
import com.olva.enviosapi.domain.model.Paquete;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos (DTO) para la solicitud de creación de un nuevo envío.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvioRequestDTO {
  @NotNull(message = "Los datos del remitente son obligatorios")
  @Valid
  private Cliente remitente;

  @NotNull(message = "Los datos del paquete son obligatorios")
  @Valid
  private Paquete datosPaquete;

  private String tipoPago;
}
