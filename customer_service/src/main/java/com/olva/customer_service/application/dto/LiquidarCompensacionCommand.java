package com.olva.customer_service.application.dto;

/**
 * DTO de entrada: mensaje que el orquestador BPM (Bonita) publica en la cola
 * {@code reclamos.compensacion.queue} para iniciar el proceso de liquidación.
 *
 * @param numeroTracking    número de tracking del envío afectado
 * @param valorDeclarado    valor declarado del paquete en soles (PEN)
 * @param flete             costo del flete pagado por el cliente en soles (PEN)
 * @param tieneSeguro       si el envío tenía seguro contratado
 * @param tieneBoleta       si el cliente presenta boleta de compra
 * @param tipoDevolucion    tipo asignado por operaciones: FLETE | COMPENSACION_COMPLETA | NOTA_CREDITO
 * @param correoCliente     correo del cliente para notificaciones
 * @param motivoReclamo     descripción del motivo del reclamo
 * @param dictamenOperaciones dictamen emitido por el área de operaciones
 */
public record LiquidarCompensacionCommand(
        String  numeroTracking,
        Double  valorDeclarado,
        Double  flete,
        Boolean tieneSeguro,
        Boolean tieneBoleta,
        String  tipoDevolucion,
        String  correoCliente,
        String  motivoReclamo,
        String  dictamenOperaciones
) {}

