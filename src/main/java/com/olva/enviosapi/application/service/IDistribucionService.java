package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.ManifiestoInputDTO;
import com.olva.enviosapi.application.dto.ConfirmacionCargaDTO;
import com.olva.enviosapi.application.dto.DetalleNovedadDTO;
import com.olva.enviosapi.domain.model.ManifiestoCarga;

import java.util.List;

public interface IDistribucionService {

  // Inicia la instancia del proceso
  void iniciarProcesoDistribucion(ManifiestoInputDTO inputDTO);

  // Tarea: Cargar mercancía (Llama al engine de Bonita para avanzar)
  void registrarCargaMercancia(String codigoManifiesto, ConfirmacionCargaDTO confirmacionDTO);

  // Tarea: Registrar recepción
  void registrarRecepcionMercancia(String codigoManifiesto, ConfirmacionCargaDTO confirmacionDTO);

  // Tareas de excepción: Reportar incidente o registrar observación
  void registrarNovedad(String codigoManifiesto, DetalleNovedadDTO novedadDTO);

  void solicitarRecursosAsincrono(String codigoManifiesto);

  // Métodos de consulta para verificación de datos guardados
  List<ManifiestoCarga> listarManifiestos();

  ManifiestoCarga obtenerManifiesto(String codigoManifiesto);
}
