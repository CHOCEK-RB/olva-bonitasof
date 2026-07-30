-- ============================================================
--  SCRIPT DE INICIALIZACIÓN — Gestión de Reclamos
--  Microservicio: customer-service
--  Se ejecuta automáticamente en cada spring-boot:run
--  gracias a spring.sql.init.mode=always
-- ============================================================

-- ── 1. Crear la base de datos si no existe ──────────────────
CREATE DATABASE IF NOT EXISTS reclamos_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE reclamos_db;

-- ── 2. Crear la tabla de reclamos si no existe ───────────────
CREATE TABLE IF NOT EXISTS reclamos (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    numero_tracking     VARCHAR(50)     NOT NULL,
    valor_declarado     DECIMAL(12, 2)  NOT NULL,
    flete               DECIMAL(10, 2)  NOT NULL,
    monto_compensacion  DECIMAL(12, 2)  NOT NULL,
    estado              VARCHAR(20)     NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion      DATETIME        NOT NULL,
    fecha_actualizacion DATETIME,

    -- Restricciones
    CONSTRAINT pk_reclamos           PRIMARY KEY (id),
    CONSTRAINT uq_reclamos_tracking  UNIQUE      (numero_tracking),
    CONSTRAINT chk_reclamos_estado   CHECK       (estado IN ('PENDIENTE', 'EJECUTADO', 'RECHAZADO')),
    CONSTRAINT chk_valor_declarado   CHECK       (valor_declarado >= 0),
    CONSTRAINT chk_flete             CHECK       (flete >= 0),
    CONSTRAINT chk_monto_comp        CHECK       (monto_compensacion >= 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = 'Registro de reclamos de compensación procesados por el microservicio';

-- ── 3. Índices para búsquedas frecuentes ────────────────────
CREATE INDEX IF NOT EXISTS idx_reclamos_estado
    ON reclamos (estado);

CREATE INDEX IF NOT EXISTS idx_reclamos_fecha_creacion
    ON reclamos (fecha_creacion);
