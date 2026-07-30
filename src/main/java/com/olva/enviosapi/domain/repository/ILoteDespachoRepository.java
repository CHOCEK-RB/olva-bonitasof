package com.olva.enviosapi.domain.repository;

import com.olva.enviosapi.domain.model.LoteDespacho;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la gestión de persistencia de la entidad LoteDespacho.
 */
@Repository
public interface ILoteDespachoRepository extends JpaRepository<LoteDespacho, String> {

  /**
   * Busca un lote de despacho por su ruta de destino y su estado actual.
   *
   * @param rutaDestino Ruta o dirección de destino del lote.
   * @param estado Estado del lote (ej. ABIERTO, CERRADO).
   * @return Un Optional conteniendo el LoteDespacho si se encuentra.
   */
  Optional<LoteDespacho> findByRutaDestinoAndEstado(String rutaDestino, String estado);
}
