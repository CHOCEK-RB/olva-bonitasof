-- ============================================================
--  DATOS SEMILLA PostgreSQL — Gestión de Reclamos
--  Perfil: postgres  →  classpath:data-postgres.sql
--
--  Diferencia vs MariaDB:
--    • INSERT IGNORE  →  INSERT ... ON CONFLICT DO NOTHING
--      (usa el UNIQUE constraint de numero_tracking)
-- ============================================================

-- Reclamo 1: compensación completa (con boleta de compra)
INSERT INTO reclamos (
    numero_tracking, valor_declarado, flete,
    monto_compensacion, estado, fecha_creacion, fecha_actualizacion
)
VALUES (
    'OLV-2024-000001', 500.00, 25.00,
    425.00, 'EJECUTADO', NOW(), NOW()
)
ON CONFLICT (numero_tracking) DO NOTHING;

-- Reclamo 2: solo devolución de flete (sin boleta de compra)
INSERT INTO reclamos (
    numero_tracking, valor_declarado, flete,
    monto_compensacion, estado, fecha_creacion, fecha_actualizacion
)
VALUES (
    'OLV-2024-000002', 0.00, 18.50,
    18.50, 'EJECUTADO', NOW(), NOW()
)
ON CONFLICT (numero_tracking) DO NOTHING;

-- Reclamo 3: pendiente de ejecución
INSERT INTO reclamos (
    numero_tracking, valor_declarado, flete,
    monto_compensacion, estado, fecha_creacion, fecha_actualizacion
)
VALUES (
    'OLV-2024-000003', 1200.00, 45.00,
    1005.00, 'PENDIENTE', NOW(), NULL
)
ON CONFLICT (numero_tracking) DO NOTHING;
