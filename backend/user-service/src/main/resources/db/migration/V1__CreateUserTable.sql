CREATE SCHEMA IF NOT EXISTS public;
CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    profile_picture BYTEA,
    join_date DATE,
    role VARCHAR(255),
    hashed_password VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    deletion_date DATE
);
