package com.olva.customer_service.infrastructure.persistence.entity;

import com.olva.customer_service.domain.model.EstadoReclamo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "reclamos")
@Getter
@Setter
@NoArgsConstructor
public class ReclamoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_tracking", nullable = false, unique = true, length = 50)
    private String numeroTracking;

    @Column(name = "valor_declarado", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorDeclarado;

    @Column(name = "flete", nullable = false, precision = 10, scale = 2)
    private BigDecimal flete;

    @Column(name = "monto_compensacion", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoCompensacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoReclamo estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
