-- ============================================================
--  DATOS SEMILLA — Gestión de Reclamos
--  Se ejecuta después de schema.sql en cada spring-boot:run.
--  Usa INSERT IGNORE para no duplicar registros existentes.
-- ============================================================

USE reclamos_db;

-- Reclamo de ejemplo 1: envío con boleta de compra (compensación completa)
INSERT IGNORE INTO reclamos (
    numero_tracking, valor_declarado, flete,
    monto_compensacion, estado, fecha_creacion, fecha_actualizacion
) VALUES (
    'OLV-2024-000001', 500.00, 25.00,
    425.00, 'EJECUTADO', NOW(), NOW()
);

-- Reclamo de ejemplo 2: solo devolución de flete (sin boleta de compra)
INSERT IGNORE INTO reclamos (
    numero_tracking, valor_declarado, flete,
    monto_compensacion, estado, fecha_creacion, fecha_actualizacion
) VALUES (
    'OLV-2024-000002', 0.00, 18.50,
    18.50, 'EJECUTADO', NOW(), NOW()
);

-- Reclamo de ejemplo 3: pendiente de ejecución
INSERT IGNORE INTO reclamos (
    numero_tracking, valor_declarado, flete,
    monto_compensacion, estado, fecha_creacion, fecha_actualizacion
) VALUES (
    'OLV-2024-000003', 1200.00, 45.00,
    1005.00, 'PENDIENTE', NOW(), NULL
);
