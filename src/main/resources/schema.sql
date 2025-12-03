-- 1. Drop table if exists to ensure clean environment on each startup
-- This is useful for in-memory H2 database to guarantee clean state on each restart
DROP TABLE IF EXISTS organization;

-- 2. Create organization table
CREATE TABLE organization (
    -- ID as primary key, using VARCHAR to store String type
    id VARCHAR(255) PRIMARY KEY,
    -- name field
    name VARCHAR(255) NOT NULL,
    -- parent_id field, using VARCHAR to store String type
    parent_id VARCHAR(255),
    -- Creation time, using TIMESTAMP WITH TIME ZONE to store timestamp
    created_time TIMESTAMP WITH TIME ZONE NOT NULL,
    -- Update time
    updated_time TIMESTAMP WITH TIME ZONE NOT NULL
);

-- 3. Create index on parent_id field to optimize query performance
CREATE INDEX idx_organization_parent_id ON organization (parent_id);


DROP TABLE IF EXISTS users;

-- 2. Create users table
CREATE TABLE  users (
    -- ID as primary key, using VARCHAR to store String type
    id VARCHAR(255) PRIMARY KEY,
    -- username field
    username VARCHAR(255) NOT NULL,
    -- full_name field
    full_name VARCHAR(255) NOT NULL,
    -- email field
    email VARCHAR(255) NOT NULL,
    -- org_id field, using VARCHAR to store String type
    org_id VARCHAR(255),
    -- Creation time, using TIMESTAMP WITH TIME ZONE to store timestamp
    created_time TIMESTAMP WITH TIME ZONE NOT NULL,
    -- Update time
    updated_time TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Unique index for username
CREATE UNIQUE INDEX uk_user_username ON users (username);

-- Index 2: Regular index for email
CREATE INDEX idx_user_email ON users (email);

-- Index 3: Regular index for full_name
CREATE INDEX idx_user_full_name ON users (full_name);