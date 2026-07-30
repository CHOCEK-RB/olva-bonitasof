package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.DevolucionResponseDTO;
import com.olva.enviosapi.application.dto.IntentoFallidoRequestDTO;
import com.olva.enviosapi.application.dto.RecepcionAlmacenRequestDTO;
import com.olva.enviosapi.application.dto.ResolucionDevolucionRequestDTO;

import java.util.List;

public interface IDevolucionService {
  DevolucionResponseDTO registrarIntentoFallido(IntentoFallidoRequestDTO request);
  DevolucionResponseDTO recepcionarEnAlmacen(RecepcionAlmacenRequestDTO request);
  DevolucionResponseDTO consultarPorTracking(String numeroTracking);
  DevolucionResponseDTO registrarResolucion(ResolucionDevolucionRequestDTO request);
  List<DevolucionResponseDTO> listarDevoluciones();
}
