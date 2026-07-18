package com.olva.enviosapi.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paquete {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotNull(message = "El peso es obligatorio")
  @Positive(message = "El peso debe ser mayor a cero")
  private Double peso;

  @NotNull(message = "El largo es obligatorio")
  @Positive(message = "El largo debe ser mayor a cero")
  private Double largo;

  @NotNull(message = "El ancho es obligatorio")
  @Positive(message = "El ancho debe ser mayor a cero")
  private Double ancho;

  @NotNull(message = "El alto es obligatorio")
  @Positive(message = "El alto debe ser mayor a cero")
  private Double alto;

  @Positive(message = "El valor declarado debe ser positivo")
  private Double valorDeclarado;

  @NotBlank(message = "La direccion de origen es obligatoria")
  private String direccionOrigen;

  @NotBlank(message = "La direccion de destino es obligatoria")
  private String direccionDestino;
}
