package com.olva.enviosapi.application.excepcion;

public class EnvioNoEncontradoException extends RuntimeException {

    public EnvioNoEncontradoException(String numeroTracking) {
        super("No se encontró un envío con el número de tracking: " + numeroTracking);
    }
}
