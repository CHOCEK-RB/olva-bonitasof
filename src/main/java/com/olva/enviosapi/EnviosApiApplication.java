package com.olva.enviosapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de inicio de la aplicacion Spring Boot para la API de Envios Olva.
 */
@SpringBootApplication
public class EnviosApiApplication {

  /**
   * Punto de entrada principal para arrancar la aplicacion.
   *
   * @param args Argumentos de linea de comandos.
   */
  public static void main(String[] args) {
    SpringApplication.run(EnviosApiApplication.class, args);
  }

}
