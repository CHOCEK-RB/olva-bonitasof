package com.olva.enviosapi.domain.repository;

import com.olva.enviosapi.domain.model.RegistroEnvio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la gestión de datos de envíos (RegistroEnvio).
 */
@Repository
public interface IRegistroEnvioRepository extends JpaRepository<RegistroEnvio, String> {

  /**
   * Busca un registro de envío utilizando su número de seguimiento único.
   *
   * @param tracking Código de seguimiento o rótulo del envío.
   * @return Un Optional con el RegistroEnvio hallado.
   */
  Optional<RegistroEnvio> findByNumeroTracking(String tracking);
}
