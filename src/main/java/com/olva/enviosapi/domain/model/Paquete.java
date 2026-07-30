package com.olva.enviosapi.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa las características físicas y direcciones de un paquete enviado.
 */
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
  private Double peso;

  private Double valorDeclarado;

  private String contenido;

  @NotBlank(message = "La direccion de origen es obligatoria")
  private String direccionOrigen;

  @NotBlank(message = "La direccion de destino es obligatoria")
  private String direccionDestino;
}
