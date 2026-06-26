-- Crear y seleccionar la base de datos
CREATE DATABASE IF NOT EXISTS desdeaqui;
USE desdeaqui;

-- ─────────────────────────────────────
-- Tabla: roles
-- ─────────────────────────────────────
CREATE TABLE roles (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- ─────────────────────────────────────
-- Tabla: usuarios
-- ─────────────────────────────────────
CREATE TABLE usuarios (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    correo      VARCHAR(100) NOT NULL UNIQUE,
    contrasena  VARCHAR(255) NOT NULL,
    rol_id      INT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- ─────────────────────────────────────
-- Tabla: destinos
-- ─────────────────────────────────────
CREATE TABLE destinos (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    imagen      VARCHAR(255)
);

-- ─────────────────────────────────────
-- Tabla: tips
-- ─────────────────────────────────────
CREATE TABLE tips (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    contenido  TEXT NOT NULL,
    categoria  VARCHAR(50) NOT NULL,
    destino_id INT NOT NULL,
    FOREIGN KEY (destino_id) REFERENCES destinos(id)
);

-- ─────────────────────────────────────
-- Tabla: guardados
-- ─────────────────────────────────────
CREATE TABLE guardados (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id  INT NOT NULL,
    destino_id  INT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (destino_id) REFERENCES destinos(id),
    UNIQUE (usuario_id, destino_id)   -- un usuario no puede guardar el mismo destino dos veces
);