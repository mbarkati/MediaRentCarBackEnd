CREATE TABLE admin_accounts
(
    id         UUID         NOT NULL,
    username   VARCHAR(100) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,

    CONSTRAINT pk_admin_accounts PRIMARY KEY (id),
    CONSTRAINT uq_admin_accounts_username UNIQUE (username)
);
