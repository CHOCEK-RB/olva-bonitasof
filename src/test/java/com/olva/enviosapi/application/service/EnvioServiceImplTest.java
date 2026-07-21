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

}

