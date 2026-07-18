package com.olva.enviosapi.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
  private String estadoEnvio;
  private String comprobantePago;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "cliente_id")
  private Cliente remitente;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "paquete_id")
  private Paquete datosPaquete;
}
