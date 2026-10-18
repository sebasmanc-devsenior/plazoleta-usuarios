-- =============================================================================
-- PLAZOLETA DE COMIDAS - BASE DE DATOS DEL MICROSERVICIO DE USUARIOS (MySQL 8)
-- =============================================================================
-- En MySQL Workbench: conéctate como root, File > Open SQL Script, selecciona este
-- archivo y ejecútalo completo con el rayo (Ctrl+Shift+Enter).
-- Es idempotente: se puede ejecutar varias veces sin borrar datos.
-- =============================================================================

-- (OPCIONAL) Reiniciar desde cero. Descomenta solo si quieres borrar TODO.
-- DROP DATABASE IF EXISTS plazoleta_users;

CREATE DATABASE IF NOT EXISTS plazoleta_users
    CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE plazoleta_users;

-- HU-1 propietario, HU-6 empleado, HU-8 cliente. La clave se guarda con BCrypt.
CREATE TABLE IF NOT EXISTS users (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    name              VARCHAR(100) NOT NULL,
    last_name         VARCHAR(100) NOT NULL,
    identity_document VARCHAR(40)  NOT NULL,
    phone             VARCHAR(13)  NULL,
    birth_date        DATE         NULL,       -- obligatoria solo para PROPIETARIO (mayor de edad)
    email             VARCHAR(254) NOT NULL,
    password_hash     VARCHAR(100) NOT NULL,   -- hash BCrypt, nunca la clave en texto plano
    role              VARCHAR(20)  NOT NULL,
    restaurant_id     BIGINT       NULL,       -- solo para EMPLEADO
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_identity_document UNIQUE (identity_document),
    CONSTRAINT chk_users_identity_document CHECK (identity_document REGEXP '^[0-9]+$'),
    CONSTRAINT chk_users_role CHECK (role IN ('ADMIN', 'PROPIETARIO', 'EMPLEADO', 'CLIENTE')),
    CONSTRAINT chk_users_employee_restaurant CHECK (
        (role = 'EMPLEADO' AND restaurant_id IS NOT NULL) OR
        (role <> 'EMPLEADO' AND restaurant_id IS NULL)
    )
) ENGINE = InnoDB;

-- Administrador inicial (necesario para crear propietarios y restaurantes).
-- Clave: la de ADMIN_BOOTSTRAP_PASSWORD en .env.example, guardada como hash BCrypt.
INSERT IGNORE INTO users
    (name, last_name, identity_document, phone, birth_date, email, password_hash, role, restaurant_id)
VALUES
    ('Administrador', 'Principal', '100000001', '+573001234567', '1985-01-01',
     'admin@plazoleta.com',
     '$2a$10$ypdPtZPsd0piCrtuAzyFAenLUsT8bv8vJ/njhaweGPxDez2D.Rc8a',
     'ADMIN', NULL);

-- Usuario de conexión del microservicio (debe coincidir con .env)
CREATE USER IF NOT EXISTS 'plazoleta_users'@'%'         IDENTIFIED BY 'plazoleta_pass';
CREATE USER IF NOT EXISTS 'plazoleta_users'@'localhost' IDENTIFIED BY 'plazoleta_pass';
ALTER USER 'plazoleta_users'@'%'         IDENTIFIED BY 'plazoleta_pass';
ALTER USER 'plazoleta_users'@'localhost' IDENTIFIED BY 'plazoleta_pass';
GRANT ALL PRIVILEGES ON plazoleta_users.* TO 'plazoleta_users'@'%';
GRANT ALL PRIVILEGES ON plazoleta_users.* TO 'plazoleta_users'@'localhost';
FLUSH PRIVILEGES;

SELECT id, email, role FROM plazoleta_users.users;
