package com.olva.enviosapi.domain.repository;

import com.olva.enviosapi.domain.model.DevolucionEnvio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la gestión de persistencia de devoluciones de envíos.
 */
public interface IDevolucionRepository extends JpaRepository<DevolucionEnvio, String> {

  /**
   * Busca la devolución asociada a un número de tracking.
   *
   * @param numeroTracking Número de seguimiento.
   * @return Optional con DevolucionEnvio.
   */
  Optional<DevolucionEnvio> findByNumeroTracking(String numeroTracking);

  /**
   * Busca una devolución por su número de tracking y estado.
   *
   * @param numeroTracking Número de seguimiento.
   * @param estado Estado de la devolución.
   * @return Optional con DevolucionEnvio.
   */
  Optional<DevolucionEnvio> findByNumeroTrackingAndEstado(String numeroTracking, String estado);
}
