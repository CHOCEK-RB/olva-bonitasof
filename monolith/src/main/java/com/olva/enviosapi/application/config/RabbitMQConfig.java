package com.olva.enviosapi.application.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de infraestructura para RabbitMQ, exchanges, colas y serialización JSON.
 */
@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

  public static final String EXCHANGE_NAME = "olva.logistica.exchange";
  public static final String QUEUE_NAME = "almacen_queue";
  public static final String ROUTING_KEY = "envio.despachado";

  private final ConnectionFactory connectionFactory;

  /**
   * Forzar la inicialización de la conexión con el broker de RabbitMQ al arrancar.
   */
  @PostConstruct
  public void forzarInfraestructuraRabbit() {
    connectionFactory.createConnection().close();
  }

  /**
   * Define el TopicExchange principal para eventos de logística.
   *
   * @return Instancia del TopicExchange.
   */
  @Bean
  public TopicExchange logisticaExchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  /**
   * Define la cola duradera para el módulo de almacén.
   *
   * @return Instancia de la Queue.
   */
  @Bean
  public Queue almacenQueue() {
    return new Queue(QUEUE_NAME, true);
  }

  /**
   * Vincula la cola de almacén con el exchange usando la clave de enrutamiento especificada.
   *
   * @param almacenQueue Cola del almacén.
   * @param logisticaExchange Exchange de logística.
   * @return Objeto Binding.
   */
  @Bean
  public Binding binding(Queue almacenQueue, TopicExchange logisticaExchange) {
    return BindingBuilder.bind(almacenQueue).to(logisticaExchange).with(ROUTING_KEY);
  }

  /**
   * Configura el convertidor de mensajes de RabbitMQ para usar formato JSON con Jackson.
   *
   * @return Instancia de MessageConverter.
   */
  @Bean
  public MessageConverter jsonMessageConverter() {
    return new JacksonJsonMessageConverter();
  }

  /**
   * Crea y configura la plantilla RabbitTemplate con el convertidor JSON.
   *
   * @param connectionFactory Factoría de conexiones RabbitMQ.
   * @return Instancia de RabbitTemplate configurada.
   */
  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(jsonMessageConverter());
    return template;
  }
}
