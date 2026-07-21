package com.olva.enviosapi.application.dto;

import com.olva.enviosapi.domain.model.Cliente;
import com.olva.enviosapi.domain.model.Paquete;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnvioRequestDTO {
  @NotBlank(message = "El tipo de pago es obligatorio")
  private String tipoPago;

  @Valid
  @NotNull(message = "El remitente es obligatorio")
  private Cliente remitente;

  @Valid
  @NotNull(message = "Los datos del paquete son obligatorios")
  private Paquete datosPaquete;
}