package com.olva.enviosapi.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA principal que representa el registro completo de una orden de envío de paquete.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroEnvio {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private LocalDate fechaRegistro;
  private String tipoPago;
  private Double montoTotal;
  private Boolean pagoConfirmado;
  private String numeroTracking;
  private String comprobantePago;
  private String observacionesPaquete;
  private String loteId;

  @Enumerated(EnumType.STRING)
  private EstadoEnvio estado;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "cliente_id")
  private Cliente remitente;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "paquete_id")
  private Paquete datosPaquete;

  /**
   * Obtiene la dirección de origen declarada en el paquete asociado.
   *
   * @return Cadena con la dirección de origen.
   */
  public String getOrigen() {
    return datosPaquete != null ? datosPaquete.getDireccionOrigen() : null;
  }

  /**
   * Obtiene la dirección de destino del paquete.
   *
   * @return Cadena con la dirección de destino.
   */
  public String getDestino() {
    return datosPaquete != null ? datosPaquete.getDireccionDestino() : null;
  }

  /**
   * Retorna una descripción simulada de la ubicación actual del paquete según su estado.
   *
   * @return Ubicación física o logística del paquete.
   */
  public String getUbicacionActual() {
    if (estado == null) {
      return "Desconocido";
    }
    switch (estado) {
      case PENDIENTE:
        return "Oficina de Origen - En Recepción";
      case EN_TRANSITO:
        return "En Ruta hacia Centro de Distribución";
      case ENTREGADO:
        return "Entregado al Destinatario";
      default:
        return "En almacén central";
    }
  }

  /**
   * Calcula la fecha aproximada de entrega (3 días posteriores al registro).
   *
   * @return Objeto LocalDate estimado.
   */
  public java.time.LocalDateTime getFechaEntregaEstimada() {
    return fechaRegistro != null ? fechaRegistro.plusDays(3).atStartOfDay() : null;
  }
}
