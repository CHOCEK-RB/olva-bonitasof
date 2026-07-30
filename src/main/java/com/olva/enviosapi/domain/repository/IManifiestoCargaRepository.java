package com.olva.enviosapi.domain.repository;

import com.olva.enviosapi.domain.model.ManifiestoCarga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IManifiestoCargaRepository extends JpaRepository<ManifiestoCarga, Long> {

  // Búsqueda por el identificador único de negocio
  Optional<ManifiestoCarga> findByCodigoManifiesto(String codigoManifiesto);

}
