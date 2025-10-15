-- Migration V3:
-- This migration will drop all the test tables and implement the schemas provided by the
-- Facilities and Inventory Management teams

-- Drop tables if they exist (in reverse order due to foreign key constraints)
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-------------------------------
-- Facilities schema (public)
-------------------------------
CREATE TABLE IF NOT EXISTS "Machines"(
    "id" BIGINT PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS "Parts"(
    "id" BIGINT PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "machine_id" BIGINT NULL
);

CREATE TABLE IF NOT EXISTS "Logs"(
    "id" BIGINT PRIMARY KEY,
    "date" DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS "Reports"(
    "id" BIGINT PRIMARY KEY,
    "report_text" VARCHAR(255) NOT NULL,
    "log_id" BIGINT NOT NULL,
    "part_id" BIGINT NULL,
    "machine_id" BIGINT NULL,
    "needs_repair" BOOLEAN NOT NULL
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'parts_machine_id_foreign'
          AND table_name = 'Parts'
    ) THEN
        ALTER TABLE "Parts"
            ADD CONSTRAINT "parts_machine_id_foreign"
            FOREIGN KEY ("machine_id") REFERENCES "Machines"("id");
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'reports_part_id_foreign'
          AND table_name = 'Reports'
    ) THEN
        ALTER TABLE "Reports"
            ADD CONSTRAINT "reports_part_id_foreign"
            FOREIGN KEY ("part_id") REFERENCES "Parts"("id");
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'reports_log_id_foreign'
          AND table_name = 'Reports'
    ) THEN
        ALTER TABLE "Reports"
            ADD CONSTRAINT "reports_log_id_foreign"
            FOREIGN KEY ("log_id") REFERENCES "Logs"("id");
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'reports_machine_id_foreign'
          AND table_name = 'Reports'
    ) THEN
        ALTER TABLE "Reports"
            ADD CONSTRAINT "reports_machine_id_foreign"
            FOREIGN KEY ("machine_id") REFERENCES "Machines"("id");
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_reports_log_id     ON "Reports"("log_id");
CREATE INDEX IF NOT EXISTS idx_reports_machine_id ON "Reports"("machine_id");
CREATE INDEX IF NOT EXISTS idx_parts_machine_id   ON "Parts"("machine_id");

-------------------------------
-- Inventory schema
-------------------------------
CREATE SCHEMA IF NOT EXISTS "inventory";

CREATE TABLE IF NOT EXISTS "inventory"."Items" (
    "id" UUID PRIMARY KEY,
    "sku" TEXT UNIQUE,
    "name" TEXT NOT NULL,
    "reorder_point" INT NOT NULL DEFAULT 0,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS "inventory"."Stock_Levels" (
    "item_id" UUID PRIMARY KEY,
    "level" INT NOT NULL DEFAULT 0,
    "updated_at" TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS "inventory"."Stock_Ledger" (
    "id" UUID PRIMARY KEY,
    "item_id" UUID NOT NULL,
    "delta" INT NOT NULL,
    "reason" TEXT NOT NULL,
    "ref_id" TEXT UNIQUE NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now()
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_schema = 'inventory'
          AND table_name = 'Stock_Levels'
          AND constraint_name = 'stock_levels_item_id_fkey'
    ) THEN
        ALTER TABLE "inventory"."Stock_Levels"
            ADD CONSTRAINT "stock_levels_item_id_fkey"
            FOREIGN KEY ("item_id")
            REFERENCES "inventory"."Items" ("id")
            ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_schema = 'inventory'
          AND table_name = 'Stock_Ledger'
          AND constraint_name = 'stock_ledger_item_id_fkey'
    ) THEN
        ALTER TABLE "inventory"."Stock_Ledger"
            ADD CONSTRAINT "stock_ledger_item_id_fkey"
            FOREIGN KEY ("item_id")
            REFERENCES "inventory"."Items" ("id")
            ON DELETE CASCADE;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_items_sku ON "inventory"."Items"("sku");
CREATE INDEX IF NOT EXISTS idx_stock_ledger_item_id ON "inventory"."Stock_Ledger"("item_id");