# Tareas Pendientes para Spring Boot (PROCESO 2: Clasificación en Almacén)

*Nota para el agente desarrollador de la API:*
El **Proceso 1 (Recepción)** ha finalizado exitosamente. Ahora iniciamos la construcción del backend para el **Proceso 2 (ClasificaciónCarga)**.

El diagrama en Bonita BPM ha sido modificado para utilizar variables locales (Strings) y orquestar el flujo consumiendo endpoints REST. Tu misión es construir la infraestructura en Spring Boot que soporte estas operaciones.

Dado que estamos en el dominio de "Almacén", **se recomienda separar la lógica** creando paquetes/módulos dedicados (ej. `com.olva.enviosapi.application.almacen`) para mantener un buen diseño (Bounded Contexts).

---

## 🛠️ Tarea 1: Entidad de Dominio (LoteDespacho)
El Proceso 2 agrupa múltiples envíos/paquetes en "Lotes" que irán en un mismo camión hacia una ruta específica.
1. Crea la entidad `LoteDespacho` con los campos: `id` (UUID), `rutaDestino` (String), `estado` (String, ej: CREADO, EN_RUTA), `fechaCreacion`.
2. Actualiza la entidad `Envio` para que incluya una referencia al lote (puede ser una relación `@ManyToOne` o simplemente un campo `String loteId` para mantener un acoplamiento débil).

## 🛠️ Tarea 2: Endpoints REST para Bonita (Controlador de Almacén)
Crea un nuevo controlador (ej. `AlmacenController`) que exponga los siguientes endpoints que Bonita va a consumir vía Conectores REST:

1. **GET `/api/envios/tracking/{numeroTracking}`**
   - **Uso en Bonita:** Tarea *"Consultar destino del paquete"*.
   - **Acción:** Retornar los detalles del envío (especialmente `envioId` y la `direccionDestino`).

2. **PUT `/api/envios/tracking/{numeroTracking}/destino`**
   - **Uso en Bonita:** Tarea *"Revisión manual de etiqueta"*.
   - **Acción:** Recibir un nuevo `direccionDestino` en el body y actualizarlo en la base de datos (por si la máquina no pudo leer la etiqueta y el humano la corrigió).

3. **POST `/api/almacen/clasificar`**
   - **Uso en Bonita:** Tarea *"Asignar paquete a lote de ruta"*.
   - **Acción:** Recibe un JSON con `{ "envioId": "..." }`.
   - **Lógica Inteligente:** Busca el envío, lee su `direccionDestino`. Busca si ya existe un `LoteDespacho` abierto/pendiente para esa misma dirección. Si existe, asigna el envío a ese lote. Si no existe, crea un nuevo lote para esa dirección y asigna el envío.
   - **Retorno:** Retorna el `loteId` para que Bonita lo guarde en su variable de proceso.

## 🛠️ Tarea 3: El Consumidor de RabbitMQ (El Puente a Bonita)
Esta es la pieza más crítica. Spring Boot debe arrancar el Proceso 2 automáticamente.
1. Crea un servicio con un `@RabbitListener(queues = "almacen_queue")` (Asegúrate de que la clase `RabbitMQConfig` haya forzado la creación de la cola o que el listener la autodeclare).
2. Al recibir el mensaje del Proceso 1, extrae el `numeroTracking`.
3. Utiliza un `RestTemplate` o `WebClient` para hacer una petición HTTP al API nativa de Bonita Studio para **instanciar el proceso**.
   - *Consideración técnica:* Para llamar a la API de Bonita, Spring Boot primero debe hacer un POST a `http://localhost:8080/bonita/loginservice` (con las credenciales por defecto, ej. walter.bates/bpm) para obtener las cookies de sesión (`JSESSIONID` y `X-Bonita-API-Token`).
   - Luego debe hacer un POST a `http://localhost:8080/bonita/API/bpm/process/<ID_DEL_PROCESO>/instantiation` inyectando el contrato: `{"trackingInput": "tu-tracking"}`.

*Fin del documento de instrucciones.*
