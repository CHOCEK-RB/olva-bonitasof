package com.olva.enviosapi.application.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class RabbitMQConfig {

  public static final String EXCHANGE_NAME = "olva.logistica.exchange";
  public static final String QUEUE_NAME = "almacen_queue";
  public static final String ROUTING_KEY = "envio.despachado";

  @Autowired
  private ConnectionFactory connectionFactory;

  @PostConstruct
  public void forzarInfraestructuraRabbit() {
    connectionFactory.createConnection().close();
  }

  @Bean
  public TopicExchange logisticaExchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  @Bean
  public Queue almacenQueue() {
    return new Queue(QUEUE_NAME, true);
  }

  @Bean
  public Binding binding(Queue almacenQueue, TopicExchange logisticaExchange) {
    return BindingBuilder.bind(almacenQueue).to(logisticaExchange).with(ROUTING_KEY);
  }

  @Bean
  public org.springframework.amqp.support.converter.MessageConverter jsonMessageConverter() {
    return new org.springframework.amqp.support.converter.Jackson2JsonMessageConverter();
  }

  @Bean
  public org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    org.springframework.amqp.rabbit.core.RabbitTemplate template = new org.springframework.amqp.rabbit.core.RabbitTemplate(connectionFactory);
    template.setMessageConverter(jsonMessageConverter());
    return template;
  }
}
