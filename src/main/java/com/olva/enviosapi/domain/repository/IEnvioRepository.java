package com.olva.enviosapi.domain.repository;

import com.olva.enviosapi.domain.model.Envio;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEnvioRepository {
    Optional<Envio> buscarPorNumeroTracking(String numeroTracking);
}