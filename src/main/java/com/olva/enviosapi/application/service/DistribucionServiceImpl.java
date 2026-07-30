package com.olva.enviosapi.application.service;

import com.olva.enviosapi.application.dto.ManifiestoInputDTO;
import com.olva.enviosapi.application.dto.ConfirmacionCargaDTO;
import com.olva.enviosapi.application.dto.DetalleNovedadDTO;
import com.olva.enviosapi.domain.model.ManifiestoCarga;
import com.olva.enviosapi.domain.repository.IManifiestoCargaRepository;
import com.olva.enviosapi.infrastructure.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistribucionServiceImpl implements IDistribucionService {

  private final IManifiestoCargaRepository repository;
  private final RabbitTemplate rabbitTemplate;

  @Override
  @Transactional
  public void iniciarProcesoDistribucion(ManifiestoInputDTO inputDTO) {
    log.info("Iniciando proceso de distribución y persistiendo manifiesto: {}", inputDTO.getCodigoManifiesto());

    ManifiestoCarga manifiesto = repository.findByCodigoManifiesto(inputDTO.getCodigoManifiesto())
        .orElseGet(() -> ManifiestoCarga.builder()
            .codigoManifiesto(inputDTO.getCodigoManifiesto())
            .build());

    manifiesto.setOrigen(inputDTO.getOrigen());
    manifiesto.setDestino(inputDTO.getDestino());
    manifiesto.setCantidadPaquetes(inputDTO.getCantidadPaquetes());
    manifiesto.transicionarEstado("LISTO_PARA_DESPACHO");

    repository.save(manifiesto);
    log.info("Manifiesto {} guardado exitosamente en base de datos", inputDTO.getCodigoManifiesto());
  }

  @Override
  @Transactional
  public void registrarCargaMercancia(String codigoManifiesto, ConfirmacionCargaDTO confirmacionDTO) {
    log.info("Ejecutando tarea Cargar Mercancía para manifiesto: {}", codigoManifiesto);
    ManifiestoCarga manifiesto = repository.findByCodigoManifiesto(codigoManifiesto)
        .orElseThrow(() -> new IllegalArgumentException("Manifiesto no encontrado: " + codigoManifiesto));

    if (Boolean.TRUE.equals(confirmacionDTO.getConfirmarCargaInput())) {
      manifiesto.transicionarEstado("EN_TRANSITO");
      repository.save(manifiesto);
      log.info("Estado del manifiesto {} actualizado a EN_TRANSITO", codigoManifiesto);
    } else {
      log.warn("Carga no confirmada para el manifiesto {}", codigoManifiesto);
    }
  }

  @Override
  @Transactional
  public void registrarRecepcionMercancia(String codigoManifiesto, ConfirmacionCargaDTO confirmacionDTO) {
    log.info("Ejecutando tarea Registrar Recepción para manifiesto: {}", codigoManifiesto);
    ManifiestoCarga manifiesto = repository.findByCodigoManifiesto(codigoManifiesto)
        .orElseThrow(() -> new IllegalArgumentException("Manifiesto no encontrado: " + codigoManifiesto));

    if (Boolean.TRUE.equals(confirmacionDTO.getConfirmarRecepcionInput())) {
      manifiesto.transicionarEstado("RECIBIDO_EN_ALMACEN");
      repository.save(manifiesto);
      log.info("Estado del manifiesto {} actualizado a RECIBIDO_EN_ALMACEN", codigoManifiesto);
    }
  }

  @Override
  @Transactional
  public void registrarNovedad(String codigoManifiesto, DetalleNovedadDTO novedadDTO) {
    log.info("Registrando novedad para manifiesto {}: {}", codigoManifiesto, novedadDTO.getDetalleNovedadInput());
    ManifiestoCarga manifiesto = repository.findByCodigoManifiesto(codigoManifiesto)
        .orElseThrow(() -> new IllegalArgumentException("Manifiesto no encontrado: " + codigoManifiesto));

    manifiesto.setDetalleNovedad(novedadDTO.getDetalleNovedadInput());
    manifiesto.transicionarEstado("INCIDENCIA_EN_RUTA");
    repository.save(manifiesto);
    log.info("Novedad registrada y estado de manifiesto {} actualizado a INCIDENCIA_EN_RUTA", codigoManifiesto);
  }

  @Override
  @Transactional
  public void solicitarRecursosAsincrono(String codigoManifiesto) {
    log.info("Bonita ha solicitado recursos de forma asíncrona para manifiesto: {}", codigoManifiesto);

    ManifiestoCarga manifiesto = repository.findByCodigoManifiesto(codigoManifiesto)
        .orElseGet(() -> {
          log.warn("Manifiesto {} no existía previamente, creando registro base", codigoManifiesto);
          return repository.save(ManifiestoCarga.builder()
              .codigoManifiesto(codigoManifiesto)
              .origen("PENDIENTE")
              .destino("PENDIENTE")
              .cantidadPaquetes(0)
              .estadoEnvio("SOLICITANDO_RECURSOS")
              .build());
        });

    manifiesto.transicionarEstado("SOLICITANDO_RECURSOS");
    repository.save(manifiesto);

    // Payload de evento enviado a la cola de RabbitMQ
    Map<String, Object> evento = new HashMap<>();
    evento.put("codigoManifiesto", codigoManifiesto);
    evento.put("estado", "SOLICITANDO_RECURSOS");
    evento.put("timestamp", System.currentTimeMillis());

    rabbitTemplate.convertAndSend(RabbitConfig.COLA_RECURSOS, evento);

    log.info("Evento de solicitud de recursos publicado en RabbitMQ ({}) para {}", RabbitConfig.COLA_RECURSOS, codigoManifiesto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ManifiestoCarga> listarManifiestos() {
    return repository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public ManifiestoCarga obtenerManifiesto(String codigoManifiesto) {
    return repository.findByCodigoManifiesto(codigoManifiesto)
        .orElseThrow(() -> new IllegalArgumentException("Manifiesto no encontrado con código: " + codigoManifiesto));
  }
}
