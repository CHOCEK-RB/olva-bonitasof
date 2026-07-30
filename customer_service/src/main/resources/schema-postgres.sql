-- ============================================================
--  SCHEMA PostgreSQL — Gestión de Reclamos
--  Perfil: postgres  →  classpath:schema-postgres.sql
--
--  Diferencias clave vs MariaDB:
--    • No existe CREATE DATABASE IF NOT EXISTS → créala manualmente una vez
--    • AUTO_INCREMENT → BIGINT GENERATED ALWAYS AS IDENTITY
--    • ENGINE/CHARSET/COLLATE → no aplican en PostgreSQL
--    • DATETIME → TIMESTAMP
--    • CREATE INDEX IF NOT EXISTS → soportado desde PG 9.5
-- ============================================================

-- ── Tabla principal de reclamos ──────────────────────────────
CREATE TABLE IF NOT EXISTS reclamos (
    id                  BIGINT          GENERATED ALWAYS AS IDENTITY,
    numero_tracking     VARCHAR(50)     NOT NULL,
    valor_declarado     NUMERIC(12, 2)  NOT NULL,
    flete               NUMERIC(10, 2)  NOT NULL,
    monto_compensacion  NUMERIC(12, 2)  NOT NULL,
    estado              VARCHAR(20)     NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion      TIMESTAMP       NOT NULL,
    fecha_actualizacion TIMESTAMP,

    -- Restricciones
    CONSTRAINT pk_reclamos           PRIMARY KEY (id),
    CONSTRAINT uq_reclamos_tracking  UNIQUE      (numero_tracking),
    CONSTRAINT chk_reclamos_estado   CHECK       (estado IN ('PENDIENTE', 'EJECUTADO', 'RECHAZADO')),
    CONSTRAINT chk_valor_declarado   CHECK       (valor_declarado >= 0),
    CONSTRAINT chk_flete             CHECK       (flete >= 0),
    CONSTRAINT chk_monto_comp        CHECK       (monto_compensacion >= 0)
);

-- ── Índices para búsquedas frecuentes ───────────────────────
CREATE INDEX IF NOT EXISTS idx_reclamos_estado
    ON reclamos (estado);

CREATE INDEX IF NOT EXISTS idx_reclamos_fecha_creacion
    ON reclamos (fecha_creacion);
