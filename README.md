# Proyecto: API REST - Registro y Recepción de Envíos (OLVA)

## 1. Propósito

El objetivo de este proyecto es implementar los Servicios Web REST correspondientes al proceso misional de **"Registro y Recepción de Envíos"**

## 2. Modelo de Dominio (BDM)

- **`RegistroEnvio`**: Entidad central que registra los estados logísticos y financieros (Tracking, estado, monto total, tipo de pago).
- **`Cliente`**: Entidad agregada que guarda la identidad del remitente.
- **`Paquete`**: Entidad agregada (Objeto de Valor) que registra las características físicas del envío.

## 3. Pruebas y Validación (BDD)

El directorio `Pruebas de API` contiene la colección de Postman con los casos de prueba, evaluando validaciones de negocio, restricciones de datos y simulaciones transaccionales mediante variables dinámicas.
