package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.excepcion.EnvioNoEncontradoException;
import com.olva.enviosapi.domain.model.LoteDespacho;
import com.olva.enviosapi.domain.model.RegistroEnvio;
import com.olva.enviosapi.domain.repository.ILoteDespachoRepository;
import com.olva.enviosapi.domain.repository.IRegistroEnvioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlmacenService {

  private final IRegistroEnvioRepository envioRepository;
  private final ILoteDespachoRepository loteRepository;

  public String clasificarEnvio(String numeroTracking) {
    RegistroEnvio envio = envioRepository.findByNumeroTracking(numeroTracking)
        .orElseThrow(() -> new EnvioNoEncontradoException(numeroTracking));

    String destino = envio.getDestino();
    if (destino == null || destino.trim().isEmpty()) {
      throw new RuntimeException("El envio no tiene destino asignado");
    }

    // Buscar un lote abierto/pendiente para esa ruta
    Optional<LoteDespacho> loteOpt = loteRepository.findByRutaDestinoAndEstado(destino, "ABIERTO");

    LoteDespacho lote;
    if (loteOpt.isPresent()) {
      lote = loteOpt.get();
    } else {
      // Crear nuevo lote
      lote = new LoteDespacho(destino, "ABIERTO", LocalDateTime.now());
      lote = loteRepository.save(lote);
    }

    // Asignar el envio al lote
    envio.setLoteId(lote.getId());
    envioRepository.save(envio);

    return lote.getId();
  }
}
