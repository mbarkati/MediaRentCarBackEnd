-- V1 — Initial schema
-- Compatible with H2 2.x (dev/test) and PostgreSQL 15+ (prod).

CREATE TABLE cars
(
    id                  UUID            NOT NULL,
    license_plate       VARCHAR(20)     NOT NULL,
    brand               VARCHAR(100)    NOT NULL,
    model               VARCHAR(100)    NOT NULL,
    daily_rate_amount   DECIMAL(10, 2)  NOT NULL,
    daily_rate_currency VARCHAR(3)      NOT NULL,
    status              VARCHAR(20)     NOT NULL,
    created_at          TIMESTAMP       NOT NULL,
    updated_at          TIMESTAMP       NOT NULL,

    CONSTRAINT pk_cars PRIMARY KEY (id),
    CONSTRAINT uq_cars_license_plate UNIQUE (license_plate),
    CONSTRAINT chk_cars_status CHECK (status IN ('AVAILABLE', 'RENTED', 'MAINTENANCE'))
);

CREATE INDEX idx_cars_status ON cars (status);
