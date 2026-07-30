package com.olva.enviosapi.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

/**
 * Entidad JPA que representa un grupo o contenedor de envíos asignados a una misma ruta de
 * transporte.
 */
@Entity
@AllArgsConstructor
public class LoteDespacho {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String rutaDestino;
  private String estado;
  private LocalDateTime fechaCreacion;

  /**
   * Constructor protegido por defecto para JPA.
   */
  protected LoteDespacho() {}

  /**
   * Constructor de conveniencia para crear un lote indicando destino, estado inicial y fecha.
   *
   * @param rutaDestino Dirección o zona geográfica de la ruta.
   * @param estado Estado del lote (ej. ABIERTO).
   * @param fechaCreacion Fecha y hora de apertura del lote.
   */
  public LoteDespacho(String rutaDestino, String estado, LocalDateTime fechaCreacion) {
    this.rutaDestino = rutaDestino;
    this.estado = estado;
    this.fechaCreacion = fechaCreacion;
  }

  /**
   * Obtiene el identificador del lote.
   *
   * @return ID del lote.
   */
  public String getId() {
    return id;
  }
}
