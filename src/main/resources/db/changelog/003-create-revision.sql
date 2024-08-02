--liquibase formatted sql

--changeset dkuznetsov:1
CREATE TABLE IF NOT EXISTS revision
(
   id BIGSERIAL PRIMARY KEY,
   timestamp BIGINT NOT NULL,
   userr VARCHAR
);

--changeset dkuznetsov:2
CREATE TABLE IF NOT EXISTS linoleum_aud
(
    id INTEGER,
    rev BIGINT REFERENCES revision(id),
    revtype SMALLINT,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    modified_at TIMESTAMP,
    modified_by VARCHAR(255),
    image_path VARCHAR(255),
    l_name VARCHAR(255),
    price INTEGER,
    protect REAL,
    thickness REAL,
    image_path_mod BOOLEAN,
    l_name_mod BOOLEAN,
    price_mod BOOLEAN,
    protect_mod BOOLEAN,
    thickness_mod BOOLEAN
);