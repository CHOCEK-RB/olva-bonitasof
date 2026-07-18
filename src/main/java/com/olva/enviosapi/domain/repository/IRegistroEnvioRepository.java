package com.olva.enviosapi.domain.repository;

import com.olva.enviosapi.domain.model.RegistroEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRegistroEnvioRepository extends JpaRepository<RegistroEnvio, String> {
  Optional<RegistroEnvio> findByNumeroTracking(String tracking);
}
