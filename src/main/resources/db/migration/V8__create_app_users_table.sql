CREATE TABLE app_users
(
    id         UUID         NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    phone      VARCHAR(20)  NOT NULL,
    status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP    NOT NULL,

    CONSTRAINT pk_app_users PRIMARY KEY (id),
    CONSTRAINT uq_app_users_phone UNIQUE (phone)
);
