package com.olva.enviosapi.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "manifiesto_carga")
public class ManifiestoCarga {

  // Identificador técnico para la base de datos
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Identificador de negocio (Aggregate Root ID)
  @Column(name = "codigo_manifiesto", nullable = false, unique = true)
  private String codigoManifiesto;

  @Column(name = "origen", nullable = false)
  private String origen;

  @Column(name = "destino", nullable = false)
  private String destino;

  @Column(name = "placa_vehiculo")
  private String placaVehiculo; // Nullable

  @Column(name = "nombre_conductor")
  private String nombreConductor; // Nullable

  @Column(name = "estado_envio", nullable = false)
  private String estadoEnvio;

  // En dominio es Integer para aplicar validaciones numéricas
  @Column(name = "cantidad_paquetes", nullable = false)
  private Integer cantidadPaquetes;

  @Column(name = "detalle_novedad")
  private String detalleNovedad; // Nullable

  // Método de dominio para validar el cambio de estado (DDD)
  public void transicionarEstado(String nuevoEstado) {
    // Aquí podrías agregar validaciones lógicas, por ejemplo:
    // if (this.estadoEnvio.equals("Entregado")) throw new
    // IllegalStateException(...);
    this.estadoEnvio = nuevoEstado;
  }
}
