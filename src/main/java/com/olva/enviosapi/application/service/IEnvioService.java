package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.EnvioRequestDTO;
import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import com.olva.enviosapi.application.dto.EnvioTrackingResponse;
import com.olva.enviosapi.application.dto.RecepcionRequestDTO;
import java.util.List;

/**
 * Interfaz que define los métodos de servicio para la gestión del ciclo de vida de envíos.
 */
public interface IEnvioService {

  /**
   * Obtiene la lista completa de envíos registrados.
   *
   * @return Lista de DTOs con la información de los envíos.
   */
  List<EnvioResponseDTO> listarEnvios();

  /**
   * Registra una nueva solicitud de envío en estado pendiente.
   *
   * @param request Datos del paquete, remitente y forma de pago.
   * @return DTO con la confirmación del registro y el costo calculado.
   */
  EnvioResponseDTO registrarEnvio(EnvioRequestDTO request);

  /**
   * Genera el número de seguimiento (tracking) y el comprobante de pago para un envío.
   *
   * @param id Identificador único del envío.
   * @return DTO con los datos del envío actualizados.
   */
  EnvioResponseDTO generarRotuloTracking(String id);

  /**
   * Registra la recepción física del paquete en ventanilla y observaciones.
   *
   * @param id Identificador del envío.
   * @param request Datos de la recepción.
   * @return DTO con la respuesta actualizada.
   */
  EnvioResponseDTO recepcionEnvio(String id, RecepcionRequestDTO request);

  /**
   * Despacha el envío hacia el almacén publicando un evento en RabbitMQ.
   *
   * @param id Identificador único del envío.
   * @return DTO con la respuesta del despacho.
   */
  EnvioResponseDTO despacharEnvio(String id);

  /**
   * Marca el pago de un envío como verificado y confirmado.
   *
   * @param id Identificador único del envío.
   * @return DTO con la respuesta del pago.
   */
  EnvioResponseDTO confirmarPago(String id);

  /**
   * Consulta la información de seguimiento pública de un envío por su código tracking.
   *
   * @param numeroTracking Número de rotulado/tracking.
   * @return Respuesta estructurada de tracking.
   */
  EnvioTrackingResponse consultarEstado(String numeroTracking);

  /**
   * Actualiza la dirección de destino de un paquete en tránsito.
   *
   * @param numeroTracking Código tracking del envío.
   * @param nuevoDestino Nueva dirección de entrega.
   */
  void actualizarDestino(String numeroTracking, String nuevoDestino);
}
