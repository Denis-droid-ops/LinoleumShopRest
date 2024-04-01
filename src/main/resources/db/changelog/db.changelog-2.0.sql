--liquibase formatted sql

--changeset dkuznetsov:1
ALTER TABLE users
ADD COLUMN created_at TIMESTAMP,
ADD COLUMN modified_at TIMESTAMP,
ADD COLUMN created_by VARCHAR(50),
ADD COLUMN modified_by VARCHAR(50);

ALTER TABLE orders
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN modified_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(50),
    ADD COLUMN modified_by VARCHAR(50);

ALTER TABLE linoleum
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN modified_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(50),
    ADD COLUMN modified_by VARCHAR(50);

ALTER TABLE roll
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN modified_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(50),
    ADD COLUMN modified_by VARCHAR(50);