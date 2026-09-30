-- =====================================================================
-- Military Asset Management System (M.A.M.S.) Complete Database Script
-- =====================================================================

-- Step 1: Create Database
CREATE DATABASE IF NOT EXISTS military_asset_management;
USE military_asset_management;

-- Step 2: Create Tables
-- Roles table
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Bases table
CREATE TABLE IF NOT EXISTS bases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    base_code VARCHAR(20) NOT NULL UNIQUE,
    base_name VARCHAR(100) NOT NULL,
    location VARCHAR(200),
    status VARCHAR(20) DEFAULT 'ACTIVE'
);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    base_id BIGINT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (base_id) REFERENCES bases(id)
);

-- Asset categories table
CREATE TABLE IF NOT EXISTS asset_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Assets table
CREATE TABLE IF NOT EXISTS assets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_code VARCHAR(50) NOT NULL UNIQUE,
    asset_name VARCHAR(100) NOT NULL,
    category_id BIGINT NOT NULL,
    base_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES asset_categories(id),
    FOREIGN KEY (base_id) REFERENCES bases(id)
);

-- Purchases table
CREATE TABLE IF NOT EXISTS purchases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reference_number VARCHAR(50) NOT NULL UNIQUE,
    asset_id BIGINT NOT NULL,
    base_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    purchase_date DATE NOT NULL,
    supplier VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (asset_id) REFERENCES assets(id),
    FOREIGN KEY (base_id) REFERENCES bases(id)
);

-- Transfers table
CREATE TABLE IF NOT EXISTS transfers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reference_number VARCHAR(50) NOT NULL UNIQUE,
    asset_id BIGINT NOT NULL,
    from_base_id BIGINT NOT NULL,
    to_base_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    transfer_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'COMPLETED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (asset_id) REFERENCES assets(id),
    FOREIGN KEY (from_base_id) REFERENCES bases(id),
    FOREIGN KEY (to_base_id) REFERENCES bases(id)
);

-- Assignments table
CREATE TABLE IF NOT EXISTS assignments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_id BIGINT NOT NULL,
    base_id BIGINT NOT NULL,
    personnel_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    assignment_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (asset_id) REFERENCES assets(id),
    FOREIGN KEY (base_id) REFERENCES bases(id)
);

-- Expenditures table
CREATE TABLE IF NOT EXISTS expenditures (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_id BIGINT NOT NULL,
    base_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    reason VARCHAR(500),
    expenditure_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (asset_id) REFERENCES assets(id),
    FOREIGN KEY (base_id) REFERENCES bases(id)
);

-- Audit logs table
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(50) NOT NULL,
    entity VARCHAR(50),
    entity_id BIGINT,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Step 3: Seed Initial Data
-- Roles
INSERT INTO roles (id, name) VALUES (1, 'ADMIN'), (2, 'BASE_COMMANDER'), (3, 'LOGISTICS_OFFICER')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 4 Bases
INSERT INTO bases (id, base_code, base_name, location, status) VALUES
(1, 'WB', 'Western', 'Western Region', 'ACTIVE'),
(2, 'CB', 'Central', 'Central Region', 'ACTIVE'),
(3, 'SB', 'Southern', 'Southern Region', 'ACTIVE'),
(4, 'EB', 'Eastern', 'Eastern Region', 'ACTIVE')
ON DUPLICATE KEY UPDATE base_code=VALUES(base_code), base_name=VALUES(base_name), location=VALUES(location), status=VALUES(status);

-- Users (passwords are BCrypt hashed)
-- admin@mams.mil / admin123
INSERT INTO users (id, full_name, email, password, role_id, base_id) VALUES
(1, 'Admin User', 'admin@mams.mil', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1, NULL)
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- commander@mams.mil / commander123
INSERT INTO users (id, full_name, email, password, role_id, base_id) VALUES
(2, 'Base Commander', 'commander@mams.mil', '$2a$10$EqKcp1WFKAr1AIlRxy3MauFJGYNz9.Vju6VYN0SUmRHi/t.KzDFCe', 2, 1)
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- logistics@mams.mil / logistics123
INSERT INTO users (id, full_name, email, password, role_id, base_id) VALUES
(3, 'Logistics Officer', 'logistics@mams.mil', '$2a$10$8Gqm5EJvIXTt/.I5VL0Xee.9jhak.ONOB4KbljY8nSLkEIylWFamy', 3, NULL)
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- Asset Categories
INSERT INTO asset_categories (id, name) VALUES
(1, 'Armoured Vehicle'),
(2, 'Combat Vehicle'),
(3, 'Artillery'),
(4, 'Rocket System'),
(5, 'Transport Vehicle'),
(6, 'Support Vehicle'),
(7, 'Medical Vehicle'),
(8, 'Protected Vehicle'),
(9, 'Helicopter'),
(10, 'Small Arms'),
(11, 'Ammunition')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 20 Military Assets
INSERT INTO assets (id, asset_code, asset_name, category_id, base_id, quantity, status) VALUES
(1, 'AV-01', 'T-90 Bhishma', 1, 1, 15, 'AVAILABLE'),
(2, 'AV-02', 'T-72 Ajeya', 1, 1, 20, 'AVAILABLE'),
(3, 'AV-03', 'Arjun Main Battle Tank', 1, 2, 12, 'AVAILABLE'),
(4, 'CV-01', 'BMP-2', 2, 2, 25, 'AVAILABLE'),
(5, 'ART-01', 'K9 Vajra', 3, 1, 10, 'AVAILABLE'),
(6, 'ART-02', 'M777 Howitzer', 3, 3, 12, 'AVAILABLE'),
(7, 'ART-03', 'Dhanush Artillery Gun', 3, 4, 14, 'AVAILABLE'),
(8, 'RS-01', 'Pinaka Rocket System', 4, 2, 8, 'AVAILABLE'),
(9, 'TV-01', 'Ashok Leyland 6x6', 5, 1, 30, 'AVAILABLE'),
(10, 'SV-01', 'Heavy Recovery Vehicle', 6, 2, 5, 'AVAILABLE'),
(11, 'MV-01', 'Armoured Ambulance', 7, 3, 8, 'AVAILABLE'),
(12, 'PV-01', 'Infantry Protected Vehicle', 8, 4, 18, 'AVAILABLE'),
(13, 'HEL-01', 'ALH Dhruv', 9, 3, 6, 'AVAILABLE'),
(14, 'HEL-02', 'Light Combat Helicopter', 9, 3, 4, 'AVAILABLE'),
(15, 'HEL-03', 'Light Utility Helicopter', 9, 4, 6, 'AVAILABLE'),
(16, 'SA-01', 'INSAS Rifle', 10, 1, 200, 'AVAILABLE'),
(17, 'SA-02', '7.62 mm Assault Rifle', 10, 2, 150, 'AVAILABLE'),
(18, 'SA-03', 'Light Machine Gun', 10, 3, 80, 'AVAILABLE'),
(19, 'AMM-01', '155 mm Artillery Ammunition', 11, 1, 10000, 'AVAILABLE'),
(20, 'AMM-02', '81 mm Mortar Ammunition', 11, 4, 5000, 'AVAILABLE')
ON DUPLICATE KEY UPDATE asset_name=VALUES(asset_name), category_id=VALUES(category_id), base_id=VALUES(base_id), quantity=VALUES(quantity);
