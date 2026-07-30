package com.olva.enviosapi.domain.model;

/**
 * Enumeración que representa los estados posibles del ciclo de vida de un envío.
 */
public enum EstadoEnvio {
  PENDIENTE, EN_TRANSITO, ENTREGADO, CANCELADO, DEVUELTO, EN_REPARTO
}
