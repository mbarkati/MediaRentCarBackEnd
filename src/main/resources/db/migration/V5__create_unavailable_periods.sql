CREATE TABLE unavailable_periods (
    id         UUID         NOT NULL PRIMARY KEY,
    car_id     UUID         NOT NULL REFERENCES cars(id) ON DELETE CASCADE,
    start_date DATE         NOT NULL,
    end_date   DATE         NOT NULL,
    reason     VARCHAR(255)
);

CREATE INDEX idx_unavailable_periods_car_id ON unavailable_periods(car_id);
