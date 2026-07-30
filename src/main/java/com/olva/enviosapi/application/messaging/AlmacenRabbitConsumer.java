package com.olva.enviosapi.application.messaging;

import com.olva.enviosapi.application.service.BonitaService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor de eventos de RabbitMQ encargado de procesar mensajes de la cola de almacén.
 */
@Component
@RequiredArgsConstructor
public class AlmacenRabbitConsumer {

  private final BonitaService bonitaService;

  /**
   * Escucha la cola de almacén y desencadena la instanciación del proceso BPM en BonitaSoft.
   *
   * @param mensaje Mapa con el contenido del mensaje recibido desde RabbitMQ.
   */
  @RabbitListener(queues = "almacen_queue")
  public void procesarMensajeAlmacen(Map<String, Object> mensaje) {
    System.out.println("[ALMACEN] Mensaje recibido de RabbitMQ: " + mensaje);

    String numeroTracking = (String) mensaje.get("numeroTracking");
    if (numeroTracking == null) {
      System.err.println("No se encontró numeroTracking en el mensaje.");
      return;
    }

    try {
      bonitaService.instanciarProceso(numeroTracking);
    } catch (Exception e) {
      System.err.println("Error al instanciar proceso en Bonita: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
