package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import com.olva.enviosapi.domain.model.RegistroEnvio;
import com.olva.enviosapi.domain.repository.IRegistroEnvioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnvioServiceImpl implements IEnvioService {

  private final IRegistroEnvioRepository repository;

  public EnvioServiceImpl(IRegistroEnvioRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<EnvioResponseDTO> listarEnvios() {
    return repository.findAll().stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }

  private EnvioResponseDTO mapToDTO(RegistroEnvio envio) {
    EnvioResponseDTO dto = new EnvioResponseDTO();
    dto.setId(envio.getId());
    dto.setFechaRegistro(envio.getFechaRegistro());
    dto.setEstadoEnvio(envio.getEstadoEnvio());
    dto.setMontoTotal(envio.getMontoTotal());
    dto.setPagoConfirmado(envio.getPagoConfirmado());
    dto.setNumeroTracking(envio.getNumeroTracking());
    dto.setMensaje("Listado de envio");
    return dto;
  }
}
