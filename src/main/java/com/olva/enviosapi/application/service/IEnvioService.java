package com.olva.enviosapi.application.service;
import java.util.List;

import com.olva.enviosapi.application.dto.EnvioRequestDTO;
import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import com.olva.enviosapi.application.dto.EnvioTrackingResponse;

public interface IEnvioService {
  List<EnvioResponseDTO> listarEnvios();
  EnvioResponseDTO generarRotuloTracking(String id); 
  EnvioResponseDTO registrarEnvio(EnvioRequestDTO request);
  EnvioResponseDTO confirmarPago(String id);
  EnvioTrackingResponse consultarEstado(String numeroTracking);
}
