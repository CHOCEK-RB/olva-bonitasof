package com.olva.customer_service.domain.repository;

import com.olva.customer_service.domain.model.Reclamo;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (Output Port) del dominio para la persistencia de Reclamos.
 *
 * Define el contrato que la capa de infraestructura debe cumplir.
 * El dominio conoce esta interfaz pero NO conoce la implementación
 * (desacoplamiento DDD / Ports & Adapters).
 */
public interface ReclamoRepository {

    /**
     * Persiste o actualiza un reclamo.
     *
     * @param reclamo la entidad de dominio a guardar
     * @return el reclamo guardado con su ID asignado
     */
    Reclamo save(Reclamo reclamo);

    /**
     * Busca un reclamo por su identificador unico.
     *
     * @param id identificador interno del reclamo
     * @return {@link Optional} con el reclamo si existe
     */
    Optional<Reclamo> findById(Long id);

    /**
     * Busca un reclamo por su número de tracking
     *
     * @param numeroTracking número de tracking del envío
     * @return {@link Optional} con el reclamo si existe
     */
    Optional<Reclamo> findByNumeroTracking(String numeroTracking);

    /**
     * Retorna todos los reclamos registrados
     *
     * @return lista de reclamos (puede estar vacia)
     */
    List<Reclamo> findAll();
}
