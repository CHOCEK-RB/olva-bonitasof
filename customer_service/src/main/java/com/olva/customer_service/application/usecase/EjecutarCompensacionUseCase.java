package com.olva.customer_service.application.usecase;

import com.olva.customer_service.application.dto.CompensacionEjecutadaEvent;
import com.olva.customer_service.application.dto.LiquidarCompensacionCommand;
import com.olva.customer_service.domain.model.Reclamo;
import com.olva.customer_service.domain.repository.ReclamoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class EjecutarCompensacionUseCase {

    private static final Logger log = LoggerFactory.getLogger(EjecutarCompensacionUseCase.class);

    private final ReclamoRepository reclamoRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${reclamos.rabbitmq.exchange}")
    private String exchange;

    @Value("${reclamos.rabbitmq.routing-key.compensacion-respuesta}")
    private String routingKeyRespuesta;

    public EjecutarCompensacionUseCase(ReclamoRepository reclamoRepository,
                                       RabbitTemplate rabbitTemplate) {
        this.reclamoRepository = reclamoRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public void ejecutar(LiquidarCompensacionCommand command) {
        log.info("[CASO DE USO] Iniciando liquidación para tracking: {}", command.numeroTracking());

        // ── Paso 1: Liquidar Compensación (Dominio) ──────────────────────
        // Bonita envía doubles; manejamos posibles nulos asignando 0.0 por defecto
        Double valorDecDouble = command.valorDeclarado() != null ? command.valorDeclarado() : 0.0;
        Double fleteDouble = command.flete() != null ? command.flete() : 0.0;
        
        BigDecimal valorDeclarado = BigDecimal.valueOf(valorDecDouble);
        BigDecimal flete = BigDecimal.valueOf(fleteDouble);

        Reclamo reclamo = Reclamo.crear(
                command.numeroTracking(),
                valorDeclarado,
                flete
        );
        log.info("[DOMINIO] Monto compensación calculado: {} PEN para tracking {}",
                reclamo.getMontoCompensacion(), reclamo.getNumeroTracking());

        // ── Paso 2: Persistir en MariaDB (Infrastructure via Puerto) ─────
        Reclamo reclamoGuardado = reclamoRepository.save(reclamo);
        log.info("[INFRA] Reclamo persistido con ID: {}", reclamoGuardado.getId());

        // ── Paso 3: Ejecutar Compensación en Sistema ──────────────────────
        reclamoGuardado.marcarComoEjecutado();
        reclamoRepository.save(reclamoGuardado);
        log.info("[INFRA] Reclamo {} marcado como EJECUTADO", reclamoGuardado.getId());

        // ── Paso 4: Publicar evento de confirmación al BPM ───────────────
        CompensacionEjecutadaEvent evento = new CompensacionEjecutadaEvent(
                reclamoGuardado.getNumeroTracking(),
                reclamoGuardado.getMontoCompensacion(),
                reclamoGuardado.getEstado().name(),
                "Compensación ejecutada exitosamente. Monto acreditado: "
                        + reclamoGuardado.getMontoCompensacion() + " PEN",
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend(exchange, routingKeyRespuesta, evento);
        log.info("[RABBITMQ] Evento CompensacionEjecutadaEvent publicado para tracking: {}",
                evento.numeroTracking());
    }
}
