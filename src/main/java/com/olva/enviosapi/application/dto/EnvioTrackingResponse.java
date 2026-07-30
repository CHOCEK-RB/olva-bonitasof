package com.olva.enviosapi.application.dto;

import com.olva.enviosapi.domain.model.EstadoEnvio;
import java.time.LocalDateTime;
import lombok.Getter;

/**
 * DTO simplificado para la respuesta pública de la consulta de tracking de un envío.
 */
@Getter
public class EnvioTrackingResponse {
  private final String envioId;
  private final String numeroTracking;
  private final EstadoEnvio estado;
  private final String origen;
  private final String destino;
  private final String ubicacionActual;
  private final LocalDateTime fechaEntregaEstimada;

  /**
   * Constructor con los parámetros para la vista de seguimiento del paquete.
   *
   * @param envioId ID único del envío.
   * @param numeroTracking Código tracking público.
   * @param estado Estado actual del envío.
   * @param origen Ciudad u oficina de origen.
   * @param destino Dirección u oficina de destino.
   * @param ubicacionActual Ubicación reportada actual.
   * @param fechaEntregaEstimada Fecha estimada de entrega.
   */
  public EnvioTrackingResponse(String envioId, String numeroTracking, EstadoEnvio estado,
      String origen, String destino, String ubicacionActual, LocalDateTime fechaEntregaEstimada) {
    this.envioId = envioId;
    this.numeroTracking = numeroTracking;
    this.estado = estado;
    this.origen = origen;
    this.destino = destino;
    this.ubicacionActual = ubicacionActual;
    this.fechaEntregaEstimada = fechaEntregaEstimada;
  }
}
