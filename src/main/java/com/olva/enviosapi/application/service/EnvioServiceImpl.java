package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import com.olva.enviosapi.domain.model.RegistroEnvio;
import com.olva.enviosapi.domain.repository.IRegistroEnvioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
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
        .map(envio -> mapToDTO(envio, "Listado de envio"))
        .collect(Collectors.toList());
  }

  @Override
  public EnvioResponseDTO generarRotuloTracking(String id) {
    Optional<RegistroEnvio> envioOpt = repository.findById(id);
    if (envioOpt.isEmpty()) {
      return EnvioResponseDTO.builder().mensaje("ID de envio no encontrado").build();
    }

    RegistroEnvio envio = envioOpt.get();
    if (envio.getNumeroTracking() == null) {
      Random random = new Random();
      envio.setNumeroTracking("OLVA-" + (10000 + random.nextInt(90000)));
      envio.setComprobantePago("BOLETA-B" + (10000 + random.nextInt(90000)));
      envio.setEstadoEnvio("En red");
      repository.save(envio);
    }

    return mapToDTO(envio, "Rotulo y tracking generados correctamente.");
  }

  private EnvioResponseDTO mapToDTO(RegistroEnvio envio, String mensaje) {
    return EnvioResponseDTO.builder()
        .id(envio.getId())
        .fechaRegistro(envio.getFechaRegistro())
        .estadoEnvio(envio.getEstadoEnvio())
        .montoTotal(envio.getMontoTotal())
        .pagoConfirmado(envio.getPagoConfirmado())
        .numeroTracking(envio.getNumeroTracking())
        .mensaje(mensaje)
        .build();
  }
}
