package com.olva.customer_service.application.messaging;

import com.olva.customer_service.application.dto.LiquidarCompensacionCommand;
import com.olva.customer_service.application.usecase.EjecutarCompensacionUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CompensacionListener {

    private static final Logger log = LoggerFactory.getLogger(CompensacionListener.class);

    private final EjecutarCompensacionUseCase ejecutarCompensacionUseCase;

    public CompensacionListener(EjecutarCompensacionUseCase ejecutarCompensacionUseCase) {
        this.ejecutarCompensacionUseCase = ejecutarCompensacionUseCase;
    }

    @RabbitListener(queues = "${reclamos.rabbitmq.queue.compensacion}")
    public void onLiquidarCompensacion(LiquidarCompensacionCommand command) {
        log.info("[LISTENER] Mensaje recibido desde BPM — Tracking: {}, ValorDeclarado: {}, Flete: {}",
                command.numeroTracking(),
                command.valorDeclarado(),
                command.flete());

        ejecutarCompensacionUseCase.ejecutar(command);

        log.info("[LISTENER] Procesamiento completado para tracking: {}", command.numeroTracking());
    }
}
