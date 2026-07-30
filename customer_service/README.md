# Microservicio de Gestión de Reclamos - OLVA Courier (Customer Service)

Este repositorio contiene la implementación del microservicio encargado de la **Gestión de Reclamos y Compensaciones** de OLVA Courier, desarrollado de forma independiente al sistema core logístico (Monolito). 

Integra una API REST en Spring Boot y se comunica de manera asíncrona mediante RabbitMQ con el motor BPM (BonitaSoft) para orquestar los procesos de compensación.

## 1. Justificación arquitectónica: ¿Por qué un Microservicio?

La decisión de extraer el dominio de **Atención al Cliente (Reclamos)** del monolito core logístico obedece a los siguientes principios de arquitectura y negocio:

- **Desacoplamiento de Dominios (Separation of Concerns):** El monolito gestiona procesos misionales críticos y de alto volumen (Recepción y Clasificación de Carga). La gestión de reclamos es un dominio de post-venta. Separarlos evita que la complejidad de uno afecte al otro.
- **Escalabilidad Independiente:** Los patrones de tráfico son distintos. Mientras la logística requiere alta disponibilidad durante los envíos diarios, los reclamos pueden tener picos en fechas posteriores a temporadas altas o fallas masivas. Escalar este servicio de forma autónoma optimiza los recursos de infraestructura.
- **Despliegues Ágiles:** Las políticas de compensación, formularios de quejas y la lógica de atención al cliente cambian con mayor frecuencia que los procesos de almacén. Ser un microservicio permite desplegar actualizaciones sin riesgo de afectar o reiniciar el core logístico.
- **Resiliencia y Tolerancia a Fallos:** Si el monolito logístico o su base de datos principal (`olva_db`) sufren una caída, los clientes aún pueden registrar reclamos o consultar su estado en este microservicio (usando su propia base de datos `reclamos_db`), mejorando la experiencia y confiabilidad general del ecosistema.
- **Autonomía de Datos:** El microservicio cuenta con su propia base de datos y esquema aislado, asegurando que los datos de reclamos no saturen la base de datos transaccional de los envíos.

## 2. Descripción de los Procesos (Integración BPM)

El microservicio actúa como un actor dentro del ecosistema orquestado por BonitaSoft, comunicándose de manera asíncrona mediante RabbitMQ:

### Proceso: Liquidación de Compensación
1. El motor BPM publica un mensaje en la cola de entrada (`reclamos.compensacion.queue`) solicitando la liquidación de una compensación derivada de un reclamo que ha sido declarado procedente.
2. El microservicio procesa el cálculo y la lógica de negocio correspondiente.
3. Al finalizar, publica la respuesta en la cola de salida (`reclamos.compensacion.respuesta.queue`) para notificar al BPM y continuar el flujo del proceso de negocio.

## 3. Arquitectura y Tecnologías

- **Lenguaje:** Java 17
- **Framework Backend:** Spring Boot 3.3.5
- **Base de Datos:** MariaDB (Base de datos propia `reclamos_db`, auto-generada mediante scripts en el arranque)
- **Broker de Mensajería:** RabbitMQ
- **Documentación de API:** Springdoc OpenAPI (Swagger UI)
- **Construcción:** Maven

## 4. Requisitos Previos (Dependencias)

Para ejecutar este proyecto en un entorno local, se requiere:

- JDK 17
- Maven 3.8+
- MariaDB Server (Puerto 3306)
- RabbitMQ Server (Puerto 5672)

## 5. Instrucciones de Ejecución

### 5.1. Base de Datos y RabbitMQ

1. Asegurar que el servicio de MariaDB está en ejecución con el usuario root/root (según configuración en `application.yaml`). El driver conectará al servidor raíz e inicializará automáticamente la base de datos `reclamos_db` mediante los scripts `schema.sql` y `data.sql`.
2. Iniciar el servicio local de RabbitMQ para habilitar las colas de compensación.

### 5.2. Ejecución de la API REST (Spring Boot)

En la raíz del proyecto `customer_service`, ejecutar los siguientes comandos:

```bash
mvn clean install
mvn spring-boot:run
```

El microservicio iniciará en el puerto **8082** (esto evita conflictos con el monolito logístico que por defecto opera en el puerto 8081). 

### 5.3. Documentación de la API (Swagger)

Una vez iniciada la aplicación, la documentación interactiva de la API estará disponible para revisar y probar los endpoints:
- **Swagger UI:** [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
- **API Docs (JSON):** [http://localhost:8082/api-docs](http://localhost:8082/api-docs)

## 6. Pruebas de API

Se recomienda utilizar herramientas como Postman o la misma interfaz de Swagger UI para probar los endpoints REST correspondientes a la creación, gestión y liquidación de reclamos.
