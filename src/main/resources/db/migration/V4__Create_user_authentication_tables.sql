-- Migration V4: Create user authentication tables
-- This migration creates tables for user authentication and role-based access control

-- Drop existing users table if it exists (from V1/V2 migrations) to avoid conflicts
-- Also drop any dependent tables that might reference users
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Create user_roles junction table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, role_id)
);

-- Add foreign key constraints
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'user_roles_user_id_fkey'
          AND table_name = 'user_roles'
    ) THEN
        ALTER TABLE user_roles
            ADD CONSTRAINT user_roles_user_id_fkey
            FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'user_roles_role_id_fkey'
          AND table_name = 'user_roles'
    ) THEN
        ALTER TABLE user_roles
            ADD CONSTRAINT user_roles_role_id_fkey
            FOREIGN KEY (role_id) REFERENCES roles(id)
            ON DELETE CASCADE;
    END IF;
END $$;

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_roles_name ON roles(name);
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role_id ON user_roles(role_id);

-- Insert default roles
INSERT INTO roles (name, description) VALUES 
    ('USER', 'General user with CRUD access to all schemas'),
    ('FACILITIES_ADMIN', 'Administrator for Facilities team with database altering permissions'),
    ('INVENTORY_ADMIN', 'Administrator for Inventory team with database altering permissions')
ON CONFLICT (name) DO NOTHING;

-- Insert default admin user (password: admin123 - should be changed in production)
INSERT INTO users (username, password, email) VALUES 
    ('admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00y4F6e7wV4iEa', 'admin@example.com')
ON CONFLICT (username) DO NOTHING;

-- Assign admin role to default admin user
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'USER'
ON CONFLICT DO NOTHING;
