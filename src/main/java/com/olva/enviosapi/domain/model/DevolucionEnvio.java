package com.olva.enviosapi.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "devoluciones_envio")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionEnvio {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false, unique = true)
  private String numeroTracking;

  private String motivo;
  private String observaciones;
  private String estado;
  private String zonaAlmacen;
  private String decisionRemitente;

  private LocalDateTime fechaIntentoFallido;
  private LocalDateTime fechaRecepcionAlmacen;
  private LocalDateTime fechaResolucion;
}
