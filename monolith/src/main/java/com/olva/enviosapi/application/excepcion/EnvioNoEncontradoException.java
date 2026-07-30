package com.olva.enviosapi.application.excepcion;

/**
 * Excepción personalizada de negocio lanzada cuando no existe un envío con el ID o tracking dado.
 */
public class EnvioNoEncontradoException extends RuntimeException {

  /**
   * Construye la excepción especificando el identificador o número de tracking no encontrado.
   *
   * @param identificador ID o código de tracking buscado.
   */
  public EnvioNoEncontradoException(String identificador) {
    super("Envío no encontrado con el identificador: " + identificador);
  }
}
