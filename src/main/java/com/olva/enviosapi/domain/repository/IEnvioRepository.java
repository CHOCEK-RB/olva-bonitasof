package com.olva.enviosapi.domain.repository;

import com.olva.enviosapi.domain.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEnvioRepository extends JpaRepository<Envio, Long> {
  @Query("SELECT e FROM Envio e WHERE e.numeroTracking = :numeroTracking")
  Optional<Envio> buscarPorNumeroTracking(@Param("numeroTracking") String numeroTracking);
}
