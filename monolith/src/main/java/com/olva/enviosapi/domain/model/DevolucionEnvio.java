package com.olva.enviosapi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa el registro de devolución y reenvío de un paquete fallido.
 */
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
