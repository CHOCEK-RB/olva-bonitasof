package com.olva.enviosapi.domain.repository;

import com.olva.enviosapi.domain.model.LoteDespacho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILoteDespachoRepository extends JpaRepository<LoteDespacho, String> {
  Optional<LoteDespacho> findByRutaDestinoAndEstado(String rutaDestino, String estado);
}
