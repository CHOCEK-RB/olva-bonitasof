package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import java.util.List;

public interface IEnvioService {
  List<EnvioResponseDTO> listarEnvios();
}
