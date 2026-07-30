package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.DevolucionResponseDTO;
import com.olva.enviosapi.application.dto.IntentoFallidoRequestDTO;
import com.olva.enviosapi.application.dto.RecepcionAlmacenRequestDTO;
import com.olva.enviosapi.application.dto.ResolucionDevolucionRequestDTO;
import java.util.List;

/**
 * Interfaz que define los métodos de servicio para la gestión de devoluciones y reenvíos.
 */
public interface IDevolucionService {

  /**
   * Registra un intento fallido de entrega para un envío.
   *
   * @param request Datos del intento fallido.
   * @return DTO de la devolución registrada.
   */
  DevolucionResponseDTO registrarIntentoFallido(IntentoFallidoRequestDTO request);

  /**
   * Registra el ingreso físico del paquete devuelto al almacén central.
   *
   * @param request Datos de la recepción en almacén.
   * @return DTO de la devolución actualizada.
   */
  DevolucionResponseDTO recepsionarEnAlmacen(RecepcionAlmacenRequestDTO request);

  /**
   * Consulta el registro de devolución por su número de tracking.
   *
   * @param numeroTracking Número de seguimiento.
   * @return DTO con el detalle de la devolución.
   */
  DevolucionResponseDTO consultarPorTracking(String numeroTracking);

  /**
   * Registra la resolución final sobre la devolución (reintento, devolución o custodia).
   *
   * @param request Datos de la resolución.
   * @return DTO actualizado.
   */
  DevolucionResponseDTO registrarResolucion(ResolucionDevolucionRequestDTO request);

  /**
   * Lista todas las devoluciones registradas.
   *
   * @return Lista de DTOs de devoluciones.
   */
  List<DevolucionResponseDTO> listarDevoluciones();
}
