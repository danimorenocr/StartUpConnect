## Base de datos
´´´
-- Tabla: Rol
CREATE TABLE Rol (
id SERIAL PRIMARY KEY,
rol VARCHAR(50) NOT NULL
);

-- Tabla: Usuario
CREATE TABLE Usuario (
documento VARCHAR(20) PRIMARY KEY,
nombre VARCHAR(100),
email VARCHAR(100) UNIQUE,
contrasenna TEXT NOT NULL,
fecha_creacion DATE,
telefono VARCHAR(20),
foto_url TEXT,
id_rol INTEGER REFERENCES Rol(id)
);

-- Tabla: Emprendedor
CREATE TABLE Emprendedor (
documento VARCHAR(20) PRIMARY KEY REFERENCES Usuario(documento),
universidad VARCHAR(100),
programa_educativo VARCHAR(100)
);

-- Tabla: Mentor
CREATE TABLE Mentor (
documento VARCHAR(20) PRIMARY KEY REFERENCES Usuario(documento),
especialidad VARCHAR(100),
biografia VARCHAR(250),
linkedin TEXT,
anos_experiencia SMALLINT
);

-- Tabla: Startup
CREATE TABLE Startup (
id SERIAL PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
sector VARCHAR(50),
descripcion TEXT,
pagina_web TEXT,
logo_url TEXT,
pitch_url TEXT,
video_presentacion TEXT,
github TEXT,
linkedin TEXT,
instagram TEXT,
fecha_creacion DATE,
estado VARCHAR(50),
cant_likes SMALLINT,
id_emprendedor VARCHAR(20) REFERENCES Emprendedor(documento)
);

-- Tabla: Feedback
CREATE TABLE Feedback (
id SERIAL PRIMARY KEY,
id_startup INTEGER REFERENCES Startup(id),
id_mentor VARCHAR(20) REFERENCES Mentor(documento),
comentario_startup TEXT,
comentario_mentor TEXT,
calificacion_startup SMALLINT CHECK (calificacion_startup BETWEEN 1 AND 5),
calificacion_mentor SMALLINT CHECK (calificacion_mentor BETWEEN 1 AND 5),
fecha DATE
);

-- Tabla: Convocatoria
CREATE TABLE Convocatoria (
id SERIAL PRIMARY KEY,
titulo VARCHAR(100),
descripcion TEXT,
fecha_inicio DATE,
fecha_fin DATE,
requisitos TEXT,
organizador VARCHAR(100),
beneficios TEXT,
contacto VARCHAR(100),
sector_objetivo VARCHAR(100)
);

-- Tabla: Postulacion
CREATE TABLE Postulacion (
id SERIAL PRIMARY KEY,
id_startup INTEGER REFERENCES Startup(id),
id_convocatoria INTEGER REFERENCES Convocatoria(id),
nombre_proyecto VARCHAR(100),
problema_a_solucionar TEXT,
solucion TEXT,
clientes_objetivo TEXT,
diferenciador TEXT,
foto_url TEXT,
enlace_pagina_web TEXT,
numero_integrantes SMALLINT,
roles_integrantes TEXT,
dinero_ventas DECIMAL(10, 2),
habilidades_equipo TEXT,
etapa_proyecto VARCHAR(50) CHECK (etapa_proyecto IN ('idea', 'prototipo', 'producto mínimo viable', 'producto comercializado')),
necesidades_actuales TEXT,
fecha_postulacion DATE,
estado VARCHAR(50) CHECK (estado IN ('pendiente', 'aprobada', 'rechazada')),
pitch TEXT,
documento_propuesta TEXT,
video_pitch TEXT,
enlaces_adicionales TEXT
);

-- Tabla: Calendario
CREATE TABLE Calendario (
id SERIAL PRIMARY KEY,
nombre_evento VARCHAR(100),
fecha_inicio DATE,
fecha_fin DATE
);

-- Tabla: Notificacion
CREATE TABLE Notificacion (
id SERIAL PRIMARY KEY,
mensaje TEXT,
tipo VARCHAR(50) CHECK (tipo IN ('recordatorio', 'alerta', 'informativa')),
fecha DATE,
id_calendario INTEGER REFERENCES Calendario(id)
);

-- Tabla: Evento
CREATE TABLE Evento (
id SERIAL PRIMARY KEY,
titulo VARCHAR(100),
descripcion TEXT,
fecha DATE,
color VARCHAR(50),
id_calendario INTEGER REFERENCES Calendario(id)
);

-- Tabla: Etapa
CREATE TABLE Etapa (
id SERIAL PRIMARY KEY,
titulo VARCHAR(100),
descripcion TEXT,
fecha_inicio DATE,
fecha_fin DATE,
id_mentor VARCHAR(20) REFERENCES Mentor(documento)
);

-- Tabla: Tarea
CREATE TABLE Tarea (
id SERIAL PRIMARY KEY,
titulo VARCHAR(100),
descripcion TEXT,
fecha_entrega DATE,
id_etapa INTEGER REFERENCES Etapa(id)
);

-- Tabla: Entregables
CREATE TABLE Archivos (
id SERIAL PRIMARY KEY,
id_tarea INTEGER REFERENCES Tarea(id),
nombre_archivo TEXT,
ruta_archivo TEXT,
estado VARCHAR(50) CHECK (estado IN ('pendiente', 'aprobado', 'rechazado'))
);
-- Tabla: Comentarios
CREATE TABLE Comentarios (
id SERIAL PRIMARY KEY,
comentario TEXT,
);
´´´