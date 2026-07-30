package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.ConfirmacionCargaDTO;
import com.olva.enviosapi.application.dto.DetalleNovedadDTO;
import com.olva.enviosapi.application.dto.ManifiestoInputDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DistribucionServiceImplTest {

  // @Mock
  // IManifiestoCargaRepository repository; // Se descomentará al implementar
  // persistencia

  @InjectMocks
  private DistribucionServiceImpl distribucionService;

  private ManifiestoInputDTO manifiestoInputDTO;

  @BeforeEach
  void setUp() {
    manifiestoInputDTO = ManifiestoInputDTO.builder()
        .codigoManifiesto("MAN-2026-001")
        .origen("Lima")
        .destino("Moquegua")
        .cantidadPaquetes(150)
        .build();
  }

  @Test
  void iniciarProcesoDistribucion_DeberiaEjecutarseSinErrores() {
    // Arrange
    // (Configurar mocks aquí cuando existan)

    // Act & Assert
    assertDoesNotThrow(() -> distribucionService.iniciarProcesoDistribucion(manifiestoInputDTO));
  }

  @Test
  void registrarCargaMercancia_ConConfirmacionTrue_DeberiaEjecutarseSinErrores() {
    // Arrange
    String codigo = "MAN-2026-001";
    ConfirmacionCargaDTO confirmacion = ConfirmacionCargaDTO.builder()
        .confirmarCargaInput(true)
        .build();

    // Act & Assert
    assertDoesNotThrow(() -> distribucionService.registrarCargaMercancia(codigo, confirmacion));
  }

  @Test
  void registrarCargaMercancia_ConConfirmacionFalse_DeberiaEjecutarseSinErrores() {
    // Arrange
    String codigo = "MAN-2026-001";
    ConfirmacionCargaDTO confirmacion = ConfirmacionCargaDTO.builder()
        .confirmarCargaInput(false)
        .build();

    // Act & Assert
    assertDoesNotThrow(() -> distribucionService.registrarCargaMercancia(codigo, confirmacion));
  }

  @Test
  void registrarRecepcionMercancia_DeberiaEjecutarseSinErrores() {
    // Arrange
    String codigo = "MAN-2026-001";
    ConfirmacionCargaDTO confirmacion = ConfirmacionCargaDTO.builder()
        .confirmarRecepcionInput(true)
        .build();

    // Act & Assert
    assertDoesNotThrow(() -> distribucionService.registrarRecepcionMercancia(codigo, confirmacion));
  }

  @Test
  void registrarNovedad_DeberiaEjecutarseSinErrores() {
    // Arrange
    String codigo = "MAN-2026-001";
    DetalleNovedadDTO novedad = DetalleNovedadDTO.builder()
        .detalleNovedadInput("Paquete dañado durante el transporte")
        .build();

    // Act & Assert
    assertDoesNotThrow(() -> distribucionService.registrarNovedad(codigo, novedad));
  }
}
