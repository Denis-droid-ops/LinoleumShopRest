--liquibase formatted sql

--changeset dkuznetsov:1
CREATE TABLE IF NOT EXISTS revision
(
   id BIGSERIAL PRIMARY KEY,
   timestamp BIGINT NOT NULL
);

--changeset dkuznetsov:2
CREATE TABLE IF NOT EXISTS linoleum_aud
(
    id SERIAL PRIMARY KEY,
    rev BIGINT REFERENCES revision(id),
    revtype SMALLINT,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    modified_at TIMESTAMP,
    modified_by VARCHAR(255),
    image_path VARCHAR(255),
    l_name VARCHAR(255) UNIQUE,
    price INTEGER,
    protect REAL,
    thickness REAL
);