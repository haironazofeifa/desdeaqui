-- =====================================================
-- Desde Aquí — Schema de base de datos
-- Curso: Desarrollo de Software III
-- =====================================================

-- CREATE DATABASE IF NOT EXISTS desdeaqui CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE desdeaqui;

-- ─────────────────────────────────────
-- roles
-- ─────────────────────────────────────
CREATE TABLE roles (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- ─────────────────────────────────────
-- usuarios
-- ─────────────────────────────────────
CREATE TABLE usuarios (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    correo      VARCHAR(100) NOT NULL UNIQUE,
    contrasena  VARCHAR(255) NOT NULL,
    rol_id      INT NOT NULL,
    foto_perfil VARCHAR(255),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- ─────────────────────────────────────
-- destinos
-- ─────────────────────────────────────
CREATE TABLE destinos (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    imagen      VARCHAR(255),
    zona        VARCHAR(50),
    interes     VARCHAR(50),
    presupuesto VARCHAR(10)
);

-- ─────────────────────────────────────
-- tips
-- ─────────────────────────────────────
CREATE TABLE tips (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    contenido  TEXT NOT NULL,
    categoria  VARCHAR(50) NOT NULL,
    destino_id INT NOT NULL,
    autor_id   INT NULL,
    creado_en  DATETIME NOT NULL DEFAULT NOW(),
    FOREIGN KEY (destino_id) REFERENCES destinos(id),
    FOREIGN KEY (autor_id)   REFERENCES usuarios(id) ON DELETE SET NULL
);

-- ─────────────────────────────────────
-- guardados
-- ─────────────────────────────────────
CREATE TABLE guardados (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id  INT NOT NULL,
    destino_id  INT NOT NULL,
    UNIQUE (usuario_id, destino_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (destino_id) REFERENCES destinos(id)
);

-- ─────────────────────────────────────
-- puntuaciones_tips
-- ─────────────────────────────────────
CREATE TABLE puntuaciones_tips (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    tip_id     INT NOT NULL,
    usuario_id INT NOT NULL,
    estrellas  INT NOT NULL CHECK (estrellas BETWEEN 1 AND 5),
    UNIQUE (tip_id, usuario_id),
    FOREIGN KEY (tip_id)     REFERENCES tips(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- ─────────────────────────────────────
-- comentarios
-- ─────────────────────────────────────
CREATE TABLE comentarios (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    tip_id     INT NOT NULL,
    usuario_id INT NOT NULL,
    contenido  TEXT NOT NULL,
    creado_en  DATETIME NOT NULL DEFAULT NOW(),
    FOREIGN KEY (tip_id)     REFERENCES tips(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);