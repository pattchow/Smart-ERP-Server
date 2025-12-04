-- 1. Drop table if exists to ensure clean environment on each startup
-- This is useful for in-memory H2 database to guarantee clean state on each restart
DROP TABLE IF EXISTS t_organizations;

-- 2. Create organization table
CREATE TABLE t_organizations (
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
CREATE INDEX idx_organization_parent_id ON t_organizations (parent_id);


DROP TABLE IF EXISTS t_users;

-- 2. Create users table
CREATE TABLE t_users (
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
CREATE UNIQUE INDEX uk_user_username ON t_users (username);

-- Index 2: Regular index for email
CREATE INDEX idx_user_email ON t_users (email);

-- Index 3: Regular index for full_name
CREATE INDEX idx_user_full_name ON t_users (full_name);

-- 3. Drop table if exists to ensure clean environment on each startup
DROP TABLE IF EXISTS t_objectives;

-- 4. Create objectives table
CREATE TABLE t_objectives (
    -- ID as primary key, using VARCHAR to store String type
    id VARCHAR(255) PRIMARY KEY,
    -- Owner ID referencing user
    owner_id VARCHAR(255),
    -- Status of the objective
    status VARCHAR(20),
    -- Confidence level of achieving the objective
    confidence_level VARCHAR(10),
    -- Visibility setting
    visibility VARCHAR(15),
    -- Weight of the objective (0-100)
    weight VARCHAR(10),
    -- Score of the objective
    score VARCHAR(10),
    -- Title of the objective
    title VARCHAR(255),
    -- Description of the objective
    description TEXT,
    -- Start date of the objective
    start_date TIMESTAMP WITH TIME ZONE,
    -- Due date of the objective
    due_date TIMESTAMP WITH TIME ZONE,
    -- Another objective ID that this objective is aligned to
    aligned_to VARCHAR(255),
    -- Creation time, using TIMESTAMP WITH TIME ZONE to store timestamp
    created_time TIMESTAMP WITH TIME ZONE NOT NULL,
    -- Update time
    updated_time TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create index on owner_id field to optimize query performance
CREATE INDEX idx_objective_owner_id ON t_objectives (owner_id);

-- 5. Drop table if exists to ensure clean environment on each startup
DROP TABLE IF EXISTS t_key_results;

-- 6. Create key results table
CREATE TABLE t_key_results (
    -- ID as primary key, using VARCHAR to store String type
    id VARCHAR(255) PRIMARY KEY,
    -- Objective ID referencing objective
    objective_id VARCHAR(255),
    -- Owner ID referencing user
    owner_id VARCHAR(255),
    -- Title of the key result
    title VARCHAR(255),
    -- Description of the key result
    description TEXT,
    -- Progress of the key result (0-100)
    progress VARCHAR(10),
    -- Status of the key result
    status VARCHAR(20),
    -- Confidence level of achieving the key result
    confidence_level VARCHAR(10),
    -- Start date of the key result
    start_date TIMESTAMP WITH TIME ZONE,
    -- Due date of the key result
    due_date TIMESTAMP WITH TIME ZONE,
    -- Creation time, using TIMESTAMP WITH TIME ZONE to store timestamp
    created_time TIMESTAMP WITH TIME ZONE NOT NULL,
    -- Update time
    updated_time TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create index on objective_id field to optimize query performance
CREATE INDEX idx_key_result_objective_id ON t_key_results (objective_id);

-- Create index on owner_id field to optimize query performance
CREATE INDEX idx_key_result_owner_id ON t_key_results (owner_id);