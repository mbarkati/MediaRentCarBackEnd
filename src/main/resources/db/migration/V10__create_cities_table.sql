CREATE TABLE cities
(
    id         UUID         NOT NULL,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL,

    CONSTRAINT pk_cities PRIMARY KEY (id)
);
