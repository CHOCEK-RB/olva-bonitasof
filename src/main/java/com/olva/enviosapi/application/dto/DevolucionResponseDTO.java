package com.olva.enviosapi.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta estructurada para el proceso de devolución y reenvío de un paquete.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionResponseDTO {

  private String id;
  private String numeroTracking;
  private String motivo;
  private String observaciones;
  private String estado;
  private String zonaAlmacen;
  private String decisionRemitente;

  private LocalDateTime fechaIntentoFallido;
  private LocalDateTime fechaRecepcionAlmacen;
  private LocalDateTime fechaResolucion;

  // Datos adicionales de contexto para BonitaSoft
  private String remitenteNombre;
  private String remitenteCorreo;
  private String remitenteTelefono;
  private String direccionOrigen;
  private String direccionDestino;
  private String mensaje;
}
