package com.olva.customer_service.infrastructure.persistence.repository;

import com.olva.customer_service.infrastructure.persistence.entity.ReclamoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ReclamoJpaRepository extends JpaRepository<ReclamoJpaEntity, Long> {

    /**
     * Busca un reclamo por número de tracking.
     *
     * @param numeroTracking tracking del envio
     * @return {@link Optional} con la entidad JPA si existe
     */
    Optional<ReclamoJpaEntity> findByNumeroTracking(String numeroTracking);
}
