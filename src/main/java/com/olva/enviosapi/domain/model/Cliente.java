package com.olva.enviosapi.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotBlank(message = "El tipo de documento es obligatorio")
  private String tipoDocumento;

  @NotBlank(message = "El numero de documento es obligatorio")
  @Pattern(regexp = "^[0-9]{8,12}$", message = "El numero de documento debe tener entre 8 y 12 digitos")
  private String numeroDocumento;

  @NotBlank(message = "El nombre es obligatorio")
  private String nombres;

  @NotBlank(message = "Los apellidos son obligatorios")
  private String apellidos;

  @NotBlank(message = "El telefono es obligatorio")
  @Pattern(regexp = "^\\d{9}$", message = "El telefono debe tener exactamente 9 digitos")
  private String telefono;

  @NotBlank(message = "El correo es obligatorio")
  @Email(message = "El correo no tiene un formato valido")
  private String correo;
}
