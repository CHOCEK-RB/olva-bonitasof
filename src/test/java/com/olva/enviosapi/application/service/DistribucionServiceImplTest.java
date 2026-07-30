package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.ConfirmacionCargaDTO;
import com.olva.enviosapi.application.dto.DetalleNovedadDTO;
import com.olva.enviosapi.application.dto.ManifiestoInputDTO;
import com.olva.enviosapi.domain.model.ManifiestoCarga;
import com.olva.enviosapi.domain.repository.IManifiestoCargaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DistribucionServiceImplTest {

  @Mock
  private IManifiestoCargaRepository repository;

  @Mock
  private RabbitTemplate rabbitTemplate;

  @InjectMocks
  private DistribucionServiceImpl distribucionService;

  private ManifiestoInputDTO manifiestoInputDTO;
  private ManifiestoCarga manifiestoExistente;

  @BeforeEach
  void setUp() {
    manifiestoInputDTO = ManifiestoInputDTO.builder()
        .codigoManifiesto("MAN-2026-001")
        .origen("Lima")
        .destino("Moquegua")
        .cantidadPaquetes(150)
        .build();

    manifiestoExistente = ManifiestoCarga.builder()
        .id(1L)
        .codigoManifiesto("MAN-2026-001")
        .origen("Lima")
        .destino("Moquegua")
        .cantidadPaquetes(150)
        .estadoEnvio("LISTO_PARA_DESPACHO")
        .build();
  }

  @Test
  void iniciarProcesoDistribucion_DeberiaEjecutarseSinErrores() {
    // Arrange
    when(repository.findByCodigoManifiesto(anyString())).thenReturn(Optional.of(manifiestoExistente));
    when(repository.save(any(ManifiestoCarga.class))).thenReturn(manifiestoExistente);

    // Act & Assert
    assertDoesNotThrow(() -> distribucionService.iniciarProcesoDistribucion(manifiestoInputDTO));
    verify(repository, times(1)).save(any(ManifiestoCarga.class));
  }

  @Test
  void registrarCargaMercancia_ConConfirmacionTrue_DeberiaEjecutarseSinErrores() {
    // Arrange
    String codigo = "MAN-2026-001";
    ConfirmacionCargaDTO confirmacion = ConfirmacionCargaDTO.builder()
        .confirmarCargaInput(true)
        .build();

    when(repository.findByCodigoManifiesto(codigo)).thenReturn(Optional.of(manifiestoExistente));
    when(repository.save(any(ManifiestoCarga.class))).thenReturn(manifiestoExistente);

    // Act & Assert
    assertDoesNotThrow(() -> distribucionService.registrarCargaMercancia(codigo, confirmacion));
    assertEquals("EN_TRANSITO", manifiestoExistente.getEstadoEnvio());
  }

  @Test
  void registrarCargaMercancia_ConConfirmacionFalse_DeberiaEjecutarseSinErrores() {
    // Arrange
    String codigo = "MAN-2026-001";
    ConfirmacionCargaDTO confirmacion = ConfirmacionCargaDTO.builder()
        .confirmarCargaInput(false)
        .build();

    when(repository.findByCodigoManifiesto(codigo)).thenReturn(Optional.of(manifiestoExistente));

    // Act & Assert
    assertDoesNotThrow(() -> distribucionService.registrarCargaMercancia(codigo, confirmacion));
    verify(repository, never()).save(any());
  }

  @Test
  void registrarRecepcionMercancia_DeberiaEjecutarseSinErrores() {
    // Arrange
    String codigo = "MAN-2026-001";
    ConfirmacionCargaDTO confirmacion = ConfirmacionCargaDTO.builder()
        .confirmarRecepcionInput(true)
        .build();

    when(repository.findByCodigoManifiesto(codigo)).thenReturn(Optional.of(manifiestoExistente));
    when(repository.save(any(ManifiestoCarga.class))).thenReturn(manifiestoExistente);

    // Act & Assert
    assertDoesNotThrow(() -> distribucionService.registrarRecepcionMercancia(codigo, confirmacion));
    assertEquals("RECIBIDO_EN_ALMACEN", manifiestoExistente.getEstadoEnvio());
  }

  @Test
  void registrarNovedad_DeberiaEjecutarseSinErrores() {
    // Arrange
    String codigo = "MAN-2026-001";
    DetalleNovedadDTO novedad = DetalleNovedadDTO.builder()
        .detalleNovedadInput("Paquete dañado durante el transporte")
        .build();

    when(repository.findByCodigoManifiesto(codigo)).thenReturn(Optional.of(manifiestoExistente));
    when(repository.save(any(ManifiestoCarga.class))).thenReturn(manifiestoExistente);

    // Act & Assert
    assertDoesNotThrow(() -> distribucionService.registrarNovedad(codigo, novedad));
    assertEquals("INCIDENCIA_EN_RUTA", manifiestoExistente.getEstadoEnvio());
    assertEquals("Paquete dañado durante el transporte", manifiestoExistente.getDetalleNovedad());
  }

  @Test
  void solicitarRecursosAsincrono_DeberiaPublicarEnRabbitMQ() {
    // Arrange
    String codigo = "MAN-2026-001";
    when(repository.findByCodigoManifiesto(codigo)).thenReturn(Optional.of(manifiestoExistente));
    when(repository.save(any(ManifiestoCarga.class))).thenReturn(manifiestoExistente);

    // Act & Assert
    assertDoesNotThrow(() -> distribucionService.solicitarRecursosAsincrono(codigo));
    verify(rabbitTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
  }

  @Test
  void listarManifiestos_DeberiaRetornarLista() {
    // Arrange
    when(repository.findAll()).thenReturn(List.of(manifiestoExistente));

    // Act
    List<ManifiestoCarga> resultado = distribucionService.listarManifiestos();

    // Assert
    assertNotNull(resultado);
    assertEquals(1, resultado.size());
  }
}
