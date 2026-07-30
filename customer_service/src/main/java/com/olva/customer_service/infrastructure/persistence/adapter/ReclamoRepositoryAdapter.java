package com.olva.customer_service.infrastructure.persistence.adapter;

import com.olva.customer_service.domain.model.EstadoReclamo;
import com.olva.customer_service.domain.model.Reclamo;
import com.olva.customer_service.domain.repository.ReclamoRepository;
import com.olva.customer_service.infrastructure.persistence.entity.ReclamoJpaEntity;
import com.olva.customer_service.infrastructure.persistence.repository.ReclamoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia — implementa el puerto de salida {@link ReclamoRepository}.
 *
 * Actua como el "Adaptador Secundario" (Driving Adapter) en la arquitectura
 * Ports & Adapters. Traduce entre la entidad de dominio {@link Reclamo}
 * y la entidad JPA {@link ReclamoJpaEntity}, implementando el contrato
 * definido en el dominio.
 *
 * Esta clase conoce tanto el dominio como la infraestructura JPA,
 * pero el dominio nunca depende de ella.
 */
@Component
public class ReclamoRepositoryAdapter implements ReclamoRepository {

    private final ReclamoJpaRepository jpaRepository;

    public ReclamoRepositoryAdapter(ReclamoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // ──────────────────────────────────────────────────────────────────────
    // Implementacion del puerto
    // ──────────────────────────────────────────────────────────────────────

    @Override
    public Reclamo save(Reclamo reclamo) {
        ReclamoJpaEntity entity = toEntity(reclamo);
        ReclamoJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Reclamo> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Reclamo> findByNumeroTracking(String numeroTracking) {
        return jpaRepository.findByNumeroTracking(numeroTracking).map(this::toDomain);
    }

    @Override
    public List<Reclamo> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // ──────────────────────────────────────────────────────────────────────
    // Mappers (Dominio <-> JPA)
    // ──────────────────────────────────────────────────────────────────────

    /**
     * Convierte la entidad de dominio a la entidad de persistencia JPA.
     */
    private ReclamoJpaEntity toEntity(Reclamo reclamo) {
        ReclamoJpaEntity entity = new ReclamoJpaEntity();
        entity.setId(reclamo.getId());
        entity.setNumeroTracking(reclamo.getNumeroTracking());
        entity.setValorDeclarado(reclamo.getValorDeclarado());
        entity.setFlete(reclamo.getFlete());
        entity.setMontoCompensacion(reclamo.getMontoCompensacion());
        entity.setEstado(reclamo.getEstado() != null
                ? reclamo.getEstado()
                : EstadoReclamo.PENDIENTE);
        entity.setFechaCreacion(reclamo.getFechaCreacion());
        entity.setFechaActualizacion(reclamo.getFechaActualizacion());
        return entity;
    }

    /**
     * Convierte la entidad JPA de persistencia a la entidad de dominio.
     */
    private Reclamo toDomain(ReclamoJpaEntity entity) {
        Reclamo reclamo = new Reclamo();
        reclamo.setId(entity.getId());
        reclamo.setNumeroTracking(entity.getNumeroTracking());
        reclamo.setValorDeclarado(entity.getValorDeclarado());
        reclamo.setFlete(entity.getFlete());
        reclamo.setMontoCompensacion(entity.getMontoCompensacion());
        reclamo.setEstado(entity.getEstado());
        reclamo.setFechaCreacion(entity.getFechaCreacion());
        reclamo.setFechaActualizacion(entity.getFechaActualizacion());
        return reclamo;
    }
}
