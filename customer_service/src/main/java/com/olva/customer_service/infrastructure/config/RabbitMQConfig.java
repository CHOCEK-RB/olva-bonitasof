package com.olva.customer_service.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ — Infraestructura.
 *
 * Declara el Exchange, las colas (entrada y salida) y los bindings
 * necesarios para el flujo de compensacion de reclamos. Configura
 * Jackson como conversor de mensajes para serializacion/deserializacion
 * automática de JSON.
 *
 */
@Configuration
public class RabbitMQConfig {

    @Value("${reclamos.rabbitmq.exchange}")
    private String exchange;

    @Value("${reclamos.rabbitmq.queue.compensacion}")
    private String queueCompensacion;

    @Value("${reclamos.rabbitmq.queue-respuesta.compensacion}")
    private String queueCompensacionRespuesta;

    @Value("${reclamos.rabbitmq.routing-key.compensacion}")
    private String routingKeyCompensacion;

    @Value("${reclamos.rabbitmq.routing-key.compensacion-respuesta}")
    private String routingKeyCompensacionRespuesta;

    // ──────────────────────────────────────────────────────────────────────
    // Exchange
    // ──────────────────────────────────────────────────────────────────────

    /**
     * Exchange de tipo Topic para el dominio de reclamos.
     * Durable = true garantiza que survive a reinicios del broker.
     */
    @Bean
    public TopicExchange reclamosExchange() {
        return ExchangeBuilder
                .topicExchange(exchange)
                .durable(true)
                .build();
    }

    // ──────────────────────────────────────────────────────────────────────
    // Colas
    // ──────────────────────────────────────────────────────────────────────

    /** Cola de entrada: el BPM deposita aquí los comandos de liquidación. */
    @Bean
    public Queue queueCompensacion() {
        return QueueBuilder
                .durable(queueCompensacion)
                .build();
    }

    /** Cola de salida: el microservicio deposita aquí los eventos de confirmación. */
    @Bean
    public Queue queueCompensacionRespuesta() {
        return QueueBuilder
                .durable(queueCompensacionRespuesta)
                .build();
    }

    // ──────────────────────────────────────────────────────────────────────
    // Bindings
    // ──────────────────────────────────────────────────────────────────────

    @Bean
    public Binding bindingCompensacion(Queue queueCompensacion, TopicExchange reclamosExchange) {
        return BindingBuilder
                .bind(queueCompensacion)
                .to(reclamosExchange)
                .with(routingKeyCompensacion);
    }

    @Bean
    public Binding bindingCompensacionRespuesta(Queue queueCompensacionRespuesta,
                                                TopicExchange reclamosExchange) {
        return BindingBuilder
                .bind(queueCompensacionRespuesta)
                .to(reclamosExchange)
                .with(routingKeyCompensacionRespuesta);
    }

    // ──────────────────────────────────────────────────────────────────────
    // Conversor de mensajes (JSON)
    // ──────────────────────────────────────────────────────────────────────

    /**
     * Configura Jackson como conversor de mensajes AMQP.
     * Permite que los Records Java se serialicen/deserialicen automáticamente.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Registra el conversor JSON en el RabbitTemplate por defecto.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
