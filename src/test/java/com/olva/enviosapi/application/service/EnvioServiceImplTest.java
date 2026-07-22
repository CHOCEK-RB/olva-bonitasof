package com.olva.enviosapi.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.olva.enviosapi.application.dto.EnvioRequestDTO;
import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import com.olva.enviosapi.application.dto.EnvioTrackingResponse;
import com.olva.enviosapi.application.excepcion.EnvioNoEncontradoException;
import com.olva.enviosapi.domain.model.*;
import com.olva.enviosapi.domain.repository.IEnvioRepository;
import com.olva.enviosapi.domain.repository.IRegistroEnvioRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnvioServiceImplTest {

  @Mock
  private IRegistroEnvioRepository repository;

  @Mock
  private IEnvioRepository envioRepository;

  @InjectMocks
  private EnvioServiceImpl envioService;

  @Test
  void registrarEnvio_DeberiaCalcularMontoTotalCorrectamente() {
    EnvioRequestDTO request = new EnvioRequestDTO();
    request.setTipoPago("Fisico");

    Paquete paquete = Paquete.builder()
            .peso(2.5)
            .valorDeclarado(100.0)
            .build();
    request.setDatosPaquete(paquete);
    request.setRemitente(new Cliente());

    RegistroEnvio mockEnvio = RegistroEnvio.builder()
            .id("12345")
            .estadoEnvio("Recibido")
            .montoTotal(13.5)
            .build();

    when(repository.save(any(RegistroEnvio.class))).thenReturn(mockEnvio);

    EnvioResponseDTO response = envioService.registrarEnvio(request);

    assertNotNull(response);
    assertEquals(13.5, response.getMontoTotal());
    assertEquals("Recibido", response.getEstadoEnvio());
  }

  @Test
  void confirmarPago_CuandoExiste_DeberiaActualizarBanderaYRetornarResponse() {
    String envioId = "12345";
    RegistroEnvio mockEnvio = RegistroEnvio.builder()
            .id(envioId)
            .pagoConfirmado(false)
            .estadoEnvio("Recibido")
            .build();

    when(repository.findById(envioId)).thenReturn(Optional.of(mockEnvio));
    when(repository.save(any(RegistroEnvio.class))).thenReturn(mockEnvio);

    EnvioResponseDTO response = envioService.confirmarPago(envioId);

    assertNotNull(response);
    assertEquals(true, response.getPagoConfirmado());
    assertEquals("Pago confirmado exitosamente.", response.getMensaje());
  }

  @Test
  void confirmarPago_CuandoIdNoExiste_DeberiaRetornarMensajeDeError() {
    String idFalso = "99999";
    when(repository.findById(idFalso)).thenReturn(Optional.empty());

    EnvioResponseDTO response = envioService.confirmarPago(idFalso);

    assertNotNull(response);
    assertEquals("ID de envio no encontrado", response.getMensaje());
  }

  @Test
  @DisplayName("consultarEstado_CuandoExiste_DeberiaRetornarDatos")
  void consultarEstado_CuandoExiste_DeberiaRetornarDatos() {

    // ---------- GIVEN (Precondiciones / Valores de prueba) ----------
    String numeroTracking = "TRK-2026-00123";
    Envio envioExistente = new Envio(
            numeroTracking,
            EstadoEnvio.EN_TRANSITO,
            "Arequipa",
            "Lima",
            "Centro de distribución - Ica",
            LocalDateTime.of(2026, 7, 20, 9, 0),
            LocalDateTime.of(2026, 7, 24, 18, 0)
    );

    when(envioRepository.buscarPorNumeroTracking(numeroTracking))
            .thenReturn(Optional.of(envioExistente));

    // ---------- WHEN (Acción / caso de uso ejecutado) ----------
    EnvioTrackingResponse resultado = envioService.consultarEstado(numeroTracking);

    // ---------- THEN (Resultado esperado) ----------
    assertNotNull(resultado, "La respuesta no debería ser nula");
    assertEquals(numeroTracking, resultado.getNumeroTracking());
    assertEquals(EstadoEnvio.EN_TRANSITO, resultado.getEstado());
    assertEquals("Arequipa", resultado.getOrigen());
    assertEquals("Lima", resultado.getDestino());
    assertEquals("Centro de distribución - Ica", resultado.getUbicacionActual());
    assertEquals(LocalDateTime.of(2026, 7, 24, 18, 0), resultado.getFechaEntregaEstimada());

    verify(envioRepository, times(1)).buscarPorNumeroTracking(numeroTracking);
    verifyNoMoreInteractions(envioRepository);
  }

  @Test
  @DisplayName("consultarEstado_CuandoNoExiste_DeberiaLanzarExcepcion")
  void consultarEstado_CuandoNoExiste_DeberiaLanzarExcepcion() {
    String trackingInexistente = "TRK-NO-EXISTE";
    when(envioRepository.buscarPorNumeroTracking(trackingInexistente))
            .thenReturn(Optional.empty());

    assertThrows(
            EnvioNoEncontradoException.class,
            () -> envioService.consultarEstado(trackingInexistente)
    );
  }
}
