-- V2 — Align schema with revised Car domain model
--
-- Changes from V1:
--   - Remove  : license_plate (no longer a domain field)
--   - Rename  : daily_rate_amount  → daily_price
--   - Rename  : daily_rate_currency → currency
--   - Add     : year INT
--   - Update  : status CHECK constraint (RENTED/MAINTENANCE → UNAVAILABLE)
--
-- Compatible with H2 2.x (dev/test) and PostgreSQL 15+ (prod).

-- 1. Rename price columns
ALTER TABLE cars RENAME COLUMN daily_rate_amount   TO daily_price;
ALTER TABLE cars RENAME COLUMN daily_rate_currency TO currency;

-- 2. Drop license_plate unique constraint then the column
ALTER TABLE cars DROP CONSTRAINT IF EXISTS uq_cars_license_plate;
ALTER TABLE cars DROP COLUMN IF EXISTS license_plate;

-- 3. Add year column (DEFAULT covers any rows already present)
ALTER TABLE cars ADD COLUMN year INT NOT NULL DEFAULT 2020;
ALTER TABLE cars ADD CONSTRAINT chk_cars_year CHECK (year >= 1980 AND year <= 2100);

-- 4. Migrate any legacy status values and tighten the constraint
UPDATE cars SET status = 'UNAVAILABLE' WHERE status IN ('RENTED', 'MAINTENANCE');
ALTER TABLE cars DROP CONSTRAINT IF EXISTS chk_cars_status;
ALTER TABLE cars ADD CONSTRAINT chk_cars_status CHECK (status IN ('AVAILABLE', 'UNAVAILABLE'));
