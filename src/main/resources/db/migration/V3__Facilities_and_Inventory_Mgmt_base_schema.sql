-- V3__Facilities_and_Inventory_Mgmt_base_schema.sql
-- Creates base schema for Facilities (public) and Inventory (inventory schema)
-- Postgres-compatible, idempotent where safe, avoids quoted/mixed-case identifiers

-- UUID generation for inventory tables
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =========================================================
-- PUBLIC SCHEMA: FACILITIES DOMAIN
-- =========================================================

-- Logs of daily operations or maintenance
CREATE TABLE IF NOT EXISTS logs (
  id           BIGSERIAL PRIMARY KEY,
  date         DATE NOT NULL
);

-- Machines in the facility
CREATE TABLE IF NOT EXISTS machines (
  id           BIGSERIAL PRIMARY KEY,
  name         VARCHAR(255) NOT NULL UNIQUE
);

-- Parts associated with machines (nullable relationship)
CREATE TABLE IF NOT EXISTS parts (
  id           BIGSERIAL PRIMARY KEY,
  name         VARCHAR(255) NOT NULL,
  machine_id   BIGINT NULL REFERENCES machines(id) ON DELETE SET NULL
);

-- Reports referencing logs, optional part/machine, and repair flag
CREATE TABLE IF NOT EXISTS reports (
  id           BIGSERIAL PRIMARY KEY,
  report_text  TEXT NOT NULL,
  log_id       BIGINT NOT NULL REFERENCES logs(id) ON DELETE CASCADE,
  part_id      BIGINT NULL REFERENCES parts(id) ON DELETE SET NULL,
  machine_id   BIGINT NULL REFERENCES machines(id) ON DELETE SET NULL,
  needs_repair BOOLEAN NOT NULL
);

-- Helpful indexes
CREATE INDEX IF NOT EXISTS idx_reports_log_id        ON reports (log_id);
CREATE INDEX IF NOT EXISTS idx_reports_machine_id    ON reports (machine_id);
CREATE INDEX IF NOT EXISTS idx_parts_machine_id      ON parts (machine_id);

-- =========================================================
-- INVENTORY SCHEMA: ITEMS, STOCK LEVELS, LEDGER
-- =========================================================

CREATE SCHEMA IF NOT EXISTS inventory;

-- Master data for inventory items
CREATE TABLE IF NOT EXISTS inventory.items (
  id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  sku            TEXT UNIQUE,
  name           TEXT NOT NULL,
  reorder_point  INT NOT NULL DEFAULT 0,
  created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Current stock level per item (1:1)
CREATE TABLE IF NOT EXISTS inventory.stock_levels (
  item_id     UUID PRIMARY KEY
              REFERENCES inventory.items(id) ON DELETE CASCADE,
  level       INT NOT NULL DEFAULT 0,
  updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Immutable movement log for stock changes
CREATE TABLE IF NOT EXISTS inventory.stock_ledger (
  id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  item_id     UUID NOT NULL
              REFERENCES inventory.items(id) ON DELETE CASCADE,
  delta       INT NOT NULL,               -- positive for inbound, negative for outbound
  reason      TEXT NOT NULL,              -- e.g., "purchase", "adjustment", "usage"
  ref_id      TEXT UNIQUE NOT NULL,       -- external reference (PO#, WO#, etc.)
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Helpful indexes
CREATE INDEX IF NOT EXISTS idx_items_sku                  ON inventory.items (sku);
CREATE INDEX IF NOT EXISTS idx_stock_ledger_item_id       ON inventory.stock_ledger (item_id);

-- =========================================================
-- (Optional) simple updated_at maintenance triggers
-- Uncomment if you want automatic updated_at refreshes
-- =========================================================
-- CREATE OR REPLACE FUNCTION inventory_touch_updated_at() RETURNS trigger AS $$
-- BEGIN
--   NEW.updated_at := now();
--   RETURN NEW;
-- END; $$ LANGUAGE plpgsql;
--
-- DO $$
-- BEGIN
--   IF NOT EXISTS (
--     SELECT 1 FROM pg_trigger WHERE tgname = 'trg_items_touch_updated_at'
--   ) THEN
--     CREATE TRIGGER trg_items_touch_updated_at
--     BEFORE UPDATE ON inventory.items
--     FOR EACH ROW EXECUTE FUNCTION inventory_touch_updated_at();
--   END IF;
--   IF NOT EXISTS (
--     SELECT 1 FROM pg_trigger WHERE tgname = 'trg_stock_levels_touch_updated_at'
--   ) THEN
--     CREATE TRIGGER trg_stock_levels_touch_updated_at
--     BEFORE UPDATE ON inventory.stock_levels
--     FOR EACH ROW EXECUTE FUNCTION inventory_touch_updated_at();
--   END IF;
-- END $$;
