package com.olva.enviosapi.domain.repository;

import com.olva.enviosapi.domain.model.DevolucionEnvio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IDevolucionRepository extends JpaRepository<DevolucionEnvio, String> {
  Optional<DevolucionEnvio> findByNumeroTracking(String numeroTracking);
  Optional<DevolucionEnvio> findByNumeroTrackingAndEstado(String numeroTracking, String estado);
}
