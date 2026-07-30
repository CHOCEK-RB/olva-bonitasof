package com.olva.customer_service.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests unitarios para la entidad de dominio {@link Reclamo}.
 *
 * <p>Validan la lógica de negocio pura de liquidación de compensación
 * sin dependencias de Spring, JPA ni RabbitMQ.
 */
@DisplayName("Reclamo - Domain Unit Tests")
class ReclamoTest {

    @Test
    @DisplayName("liquidarCompensacion() debe calcular (valorDeclarado * 0.80) + flete correctamente")
    void debeCalcularMontoCompensacionCorrectamente() {
        // Arrange
        BigDecimal valorDeclarado = new BigDecimal("500.00"); // S/ 500
        BigDecimal flete = new BigDecimal("25.00");           // S/ 25

        // Act
        Reclamo reclamo = Reclamo.crear("OLV-2024-001", valorDeclarado, flete);

        // Assert
        // montoEsperado = (500 * 0.80) + 25 = 400 + 25 = 425.00
        BigDecimal montoEsperado = new BigDecimal("425.00");
        assertThat(reclamo.getMontoCompensacion())
                .isEqualByComparingTo(montoEsperado);
    }

    @Test
    @DisplayName("Nuevo reclamo debe crearse en estado PENDIENTE")
    void nuevoReclamoDebeIniciarEnEstadoPendiente() {
        // Act
        Reclamo reclamo = Reclamo.crear("OLV-2024-002",
                new BigDecimal("200.00"), new BigDecimal("15.00"));

        // Assert
        assertThat(reclamo.getEstado()).isEqualTo(EstadoReclamo.PENDIENTE);
    }

    @Test
    @DisplayName("marcarComoEjecutado() debe cambiar estado a EJECUTADO")
    void debeMarcarseComoEjecutadoCorrectamente() {
        // Arrange
        Reclamo reclamo = Reclamo.crear("OLV-2024-003",
                new BigDecimal("300.00"), new BigDecimal("20.00"));

        // Act
        reclamo.marcarComoEjecutado();

        // Assert
        assertThat(reclamo.getEstado()).isEqualTo(EstadoReclamo.EJECUTADO);
        assertThat(reclamo.getFechaActualizacion()).isNotNull();
    }

    @Test
    @DisplayName("liquidarCompensacion() con valorDeclarado=0 debe retornar solo el flete")
    void conValorDeclaradoCeroRetornaSoloFlete() {
        // Arrange
        BigDecimal valorDeclarado = BigDecimal.ZERO;
        BigDecimal flete = new BigDecimal("30.00");

        // Act
        Reclamo reclamo = Reclamo.crear("OLV-2024-004", valorDeclarado, flete);

        // Assert: (0 * 0.80) + 30 = 30.00
        assertThat(reclamo.getMontoCompensacion())
                .isEqualByComparingTo(new BigDecimal("30.00"));
    }

    @Test
    @DisplayName("liquidarCompensacion() lanza excepción si valorDeclarado es nulo")
    void debeLanzarExcepcionSiValorDeclaradoEsNulo() {
        // Arrange
        Reclamo reclamo = new Reclamo();
        reclamo.setFlete(new BigDecimal("10.00"));
        // valorDeclarado = null (no se setea)

        // Act & Assert
        assertThatThrownBy(reclamo::liquidarCompensacion)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("valorDeclarado y flete son requeridos");
    }
}
