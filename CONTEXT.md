# Contexto y Documentación de la API REST - Olva Courier (Lab 7)

Este documento proporciona una visión general y un mapa rápido de todos los Endpoints, DTOs y Modelos de Dominio que actualmente componen la API REST del proyecto `olva-bonitasof`. Sirve como punto de referencia para entender cómo funciona la aplicación, qué datos recibe y qué información devuelve, especialmente útil para integraciones futuras como Bonita BPM o RabbitMQ.

---

## 🏗️ 1. Estructuras de Datos y DTOs (Data Transfer Objects)

Los DTOs se utilizan para aislar nuestra base de datos (Entidades JPA) del exterior, definiendo exactamente qué entra y qué sale de nuestra API.

### `EnvioRequestDTO` (Datos de Entrada)
Se utiliza para recibir la información inicial cuando un cliente o ventanilla registra un nuevo paquete.
**Atributos Principales:**
- `tipoPago` (String): Opcional al inicio (ej. "Efectivo", "Tarjeta"). Si no se envía, se asigna "Pendiente".
- `remitente` (Objeto `Cliente`): Datos de quien envía (nombre, dni, direccion, telefono).
- `datosPaquete` (Objeto `Paquete`): Dimensiones, peso y descripción de la carga.

### `RecepcionRequestDTO` (Datos de Entrada para Recepción Física)
Se utiliza en el mostrador para asentar la decisión final de pago y agregar notas.
**Atributos Principales:**
- `tipoPago` (String): Ahora sí es obligatorio (Físico u Online).
- `observacionesPaquete` (String): Notas adicionales sobre el estado de la carga.

### `EnvioResponseDTO` (Datos de Salida Generales)
Es la respuesta estándar unificada para casi todas las operaciones. Oculta detalles irrelevantes de la base de datos y muestra solo lo que el cliente o sistema externo necesita saber.
**Atributos Principales:**
- `id` (String): Identificador único interno.
- `numeroTracking` (String): Código de seguimiento público (ej. "OLVA-10293").
- `estadoEnvio` (String/Enum): Estado logístico (RECIBIDO, CLASIFICANDO, EN_TRANSITO, ENTREGADO).
- `montoTotal` (Double): Costo calculado del flete.
- `pagoConfirmado` (Boolean): Indica si ya se validó el pago.
- `fechaRegistro` (LocalDate): Cuándo se ingresó al sistema.
- `mensaje` (String): Mensaje de éxito o informativo genérico.

### `EnvioTrackingResponse` (Datos de Salida para Trazabilidad)
Es una respuesta especializada, de solo lectura, exclusiva para el cliente final que consulta en la web pública dónde está su paquete.
**Atributos Principales:**
- `numeroTracking` (String)
- `estado` (EstadoEnvio)
- `origen` (String)
- `destino` (String)
- `ubicacionActual` (String)
- `fechaEntregaEstimada` (LocalDateTime)

---

## 🚀 2. Endpoints y Controladores REST (`EnvioController`)

El controlador principal `EnvioController` expone los siguientes 5 servicios. Cada uno atiende un paso crucial de la cadena de valor:

### 1. Consultar todos los envíos (Historial Administrativo)
- **Endpoint:** `GET /api/envios`
- **Descripción:** Devuelve un listado completo de todos los envíos almacenados en la base de datos MariaDB. Usado principalmente para paneles de control (dashboards) o interfaces administrativas.
- **Entrada:** Ninguna (Query params opcionales en el futuro).
- **Salida:** `List<EnvioResponseDTO>` (Arreglo JSON con los envíos).

### 2. Registrar un Nuevo Envío (Admisión)
- **Endpoint:** `POST /api/envios`
- **Descripción:** Crea un nuevo registro en el sistema con los datos físicos del paquete y del cliente. Calcula el monto a cobrar y asigna el estado inicial `RECIBIDO`.
- **Entrada:** `EnvioRequestDTO` (JSON en el Body con datos de remitente y paquete).
- **Salida:** `EnvioResponseDTO` (JSON con el ID generado, monto calculado a cobrar y estado inicial).

### 3. Generar Rótulo y Tracking Logístico
- **Endpoint:** `PUT /api/envios/{id}/generar-rotulo`
- **Descripción:** Inicia el ciclo logístico del envío asignándole un código de tracking universal (ej. "OLVA-XXXX") para que pueda ser monitoreado en ruta y pegado físicamente en la caja.
- **Entrada:** `id` del envío en la URL (Path Variable).
- **Salida:** `EnvioResponseDTO` (Actualizado con el `numeroTracking` asignado).

### 4. Confirmar Pago
- **Endpoint:** `PUT /api/envios/{id}/pago`
- **Descripción:** Valida y marca financieramente el envío como pagado (`pagoConfirmado = true`), liberándolo para poder salir de la sucursal de origen.
- **Entrada:** `id` del envío en la URL (Path Variable).
- **Salida:** `EnvioResponseDTO` (Actualizado reflejando el pago exitoso y/o comprobante generado).

### 5. Seguimiento Público / Trazabilidad (Tracking)
- **Endpoint:** `GET /api/envios/tracking/{tracking}`
- **Descripción:** Permite a un usuario externo buscar en tiempo real en qué ciudad o estado logístico se encuentra su caja, usando únicamente su código de seguimiento impreso en la boleta.
- **Entrada:** `tracking` (Código de seguimiento como "OLVA-10293") en la URL (Path Variable).
- **Salida:** `EnvioTrackingResponse` (JSON filtrado y seguro solo con los datos de ubicación, origen, destino y fecha estimada, sin exponer montos financieros o datos personales críticos).

### 6. Recepción en Ventanilla
- **Endpoint:** `PUT /api/envios/{id}/recepcion`
- **Descripción:** Actualiza el tipo de pago definitivo y las observaciones del paquete cuando el cliente entrega físicamente la carga en la oficina.
- **Entrada:** `id` en la URL y `RecepcionRequestDTO` en el Body.
- **Salida:** `EnvioResponseDTO`.

---

## 🏛️ 3. Modelo de Dominio Base (Entidades JPA)
Internamente, todo esto aterriza en la entidad `RegistroEnvio`, la cual se mapea a la tabla `registro_envios` en **MariaDB** gracias a Hibernate (Spring Data JPA). Las clases `Cliente` y `Paquete` son clases embebibles (`@Embedded`) que agrupan lógicamente columnas en la misma tabla de base de datos para mantener un rendimiento óptimo de lectura/escritura. Se agregó recientemente el campo `observacionesPaquete`.

---

## 🐇 4. Infraestructura de Mensajería Asíncrona (RabbitMQ)
El proyecto ha sido preparado para integrarse con RabbitMQ, añadiendo la dependencia AMQP.
- **Configuración (`RabbitMQConfig.java`):** Se ha predefinido el Exchange `olva.logistica.exchange` (de tipo TopicExchange) listo para emitir eventos (por ejemplo, cuando se confirme un pago o un rótulo) hacia el motor de Bonita BPM u otros sistemas de almacén.
