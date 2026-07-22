package com.olva.enviosapi.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.olva.enviosapi.application.dto.EnvioRequestDTO;
import com.olva.enviosapi.application.dto.EnvioResponseDTO;
import com.olva.enviosapi.domain.model.Cliente;
import com.olva.enviosapi.domain.model.Paquete;
import com.olva.enviosapi.domain.model.RegistroEnvio;
import com.olva.enviosapi.domain.repository.IRegistroEnvioRepository;

@ExtendWith(MockitoExtension.class)
class EnvioServiceImplTest {

  @Mock
  private IRegistroEnvioRepository repository;

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
    // 1. GIVEN: dado ID de envío válido que no ha sido pagado
    String envioId = "12345";
    RegistroEnvio mockEnvio = RegistroEnvio.builder()
            .id(envioId)
            .pagoConfirmado(false)
            .estadoEnvio("Recibido")
            .build();

    // mock: db encuentra el envio y lo guarda
    when(repository.findById(envioId)).thenReturn(java.util.Optional.of(mockEnvio));
    when(repository.save(any(RegistroEnvio.class))).thenReturn(mockEnvio);

    // 2. WHEN: entonces llamamos al método financiero confirmarPago
    EnvioResponseDTO response = envioService.confirmarPago(envioId);

    // 3. THEN: entonces esperamos que el pago esté en 'true' y retorne exito
    assertNotNull(response);
    assertEquals(true, response.getPagoConfirmado());
    assertEquals("Pago confirmado exitosamente.", response.getMensaje());
  }

  @Test
  void confirmarPago_CuandoIdNoExiste_DeberiaRetornarMensajeDeError() {
    // 1. GIVEN: dado un ID que no existe en la base de datos
    String idFalso = "99999";

    // mock repository busca ID pero devuelve vacio
    when(repository.findById(idFalso)).thenReturn(java.util.Optional.empty());

    // 2. WHEN: cuando llamamos al método
    EnvioResponseDTO response = envioService.confirmarPago(idFalso);

    // 3. THEN: entonces retorna solo el mensaje de error y no actualizar nada
    assertNotNull(response);
    assertEquals("ID de envio no encontrado", response.getMensaje());
  }

}

