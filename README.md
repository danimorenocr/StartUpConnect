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



##### INSERTS
INSERT INTO ROLES (id, rol)
VALUES
(1, 'ADMIN'),
(2, 'MENTOR'),
(3, 'EMPRENDEDOR'),
(4, 'USUARIO');

INSERT INTO USUARIOS (documento, nombre, email, contrasenna, fecha_creacion, telefono, foto_url, id_rol)
VALUES
('1234567890', 'Maria Rodriguez', 'maria@example.com', '1234', '2025-05-01', '3001234567', 'https://example.com/maria.jpg', 2),
('0987654321', 'Juan Perez', 'juan@example.com', 'abcd', '2025-04-15', '3109876543', 'https://example.com/juan.jpg', 3),
('1122334455', 'Carlos Garcia', 'carlos@example.com', 'qwerty', '2025-03-22', '3201122334', 'https://example.com/carlos.jpg', 3),
('5566778899', 'Ana Lopez', 'ana@example.com', 'pass123', '2025-02-10', '3005566778', 'https://example.com/ana.jpg', 3),
('6677889900', 'Pedro Martinez', 'pedro@example.com', 'pedro123', '2025-01-20', '3106677889', 'https://example.com/pedro.jpg', 2);

INSERT INTO EMPRENDEDORES (documento, universidad, programa_educativo, id_usuario)
VALUES
('1122334455', 'Universidad de Antioquia', 'Administración de Empresas', '1122334455'),
('5566778899', 'Universidad del Valle', 'Economía', '5566778899'),


INSERT INTO MENTORES (documento, especialidad, biografia, linkedin, anos_experiencia, id_usuario)
VALUES
('1234567890', 'Desarrollo de Software', 'Mentor especializado en proyectos de tecnología e innovación.', 'https://www.linkedin.com/in/mentor1', 5, '1234567890'),
('0987654321', 'Marketing Digital', 'Experto en estrategias de marketing para startups.', 'https://www.linkedin.com/in/mentor2', 8, '0987654321');


INSERT INTO STARTUPS (nombre, sector, descripcion, pagina_web, logo_url, pitch_url, video_presentacion, github, linkedin, instagram, fecha_creacion, estado, cant_likes, id_emprendedor)
VALUES
('TechHub', 'Tecnología', 'Plataforma para conectar desarrolladores y startups tecnológicas.', 'https://www.techhub.com', 'https://example.com/logo1.png', 'https://example.com/pitch1.pdf', 'https://example.com/video1.mp4', 'https://github.com/techhub', 'https://linkedin.com/company/techhub', 'https://instagram.com/techhub', '2023-01-15', 'Activa', 150, '1122334455'),
('EcoSmart', 'Energía Renovable', 'Soluciones sostenibles para energía limpia y eficiencia energética.', 'https://www.ecosmart.com', 'https://example.com/logo2.png', 'https://example.com/pitch2.pdf', 'https://example.com/video2.mp4', 'https://github.com/ecosmart', 'https://linkedin.com/company/ecosmart', 'https://instagram.com/ecosmart', '2023-02-10', 'Activa', 300, '5566778899');

INSERT INTO CONVOCATORIAS (titulo, descripcion, fecha_inicio, fecha_fin, requisitos, organizador, beneficios, contacto, sector_objetivo)
VALUES
('Innovación en Tecnología',
'Convocatoria para startups tecnológicas enfocadas en inteligencia artificial y aprendizaje automático.',
'2025-06-01',
'2025-08-01',
'Experiencia en desarrollo de software, prototipos funcionales, equipo técnico sólido.',
'Ministerio de Tecnología',
'Acceso a capital semilla, mentoría especializada, conexiones con inversionistas.',
'tecnologia@ministerio.com',
'Tecnología, Innovación'),

('Crecimiento Sostenible',
'Convocatoria para proyectos sostenibles y startups ecológicas que buscan impacto positivo.',
'2025-05-15',
'2025-09-15',
'Modelo de negocio sostenible, impacto ambiental medible, compromiso con prácticas ecológicas.',
'Fundación Verde',
'Financiamiento, visibilidad en medios, acceso a redes de sostenibilidad.',
'contacto@fundacionverde.org',
'Medio Ambiente, Sostenibilidad'),

('Salud y Bienestar',
'Convocatoria para innovadores en salud digital, biotecnología y bienestar.',
'2025-04-01',
'2025-07-01',
'Tecnologías innovadoras en salud, pruebas clínicas iniciales, equipo interdisciplinario.',
'Ministerio de Salud',
'Acceso a laboratorios, pruebas clínicas, conexiones con hospitales.',
'salud@ministerio.com',
'Salud, Bienestar'),

('Educación y Tecnología',
'Convocatoria para startups que están revolucionando la educación a través de tecnología.',
'2025-03-01',
'2025-06-01',
'Enfoque en tecnología educativa, impacto en aprendizaje, modelo de negocio escalable.',
'Fundación para la Educación',
'Acceso a plataformas educativas, mentoría, participación en ferias tecnológicas.',
'educacion@fundacion.org',
'Educación, Tecnología'),

('Agricultura Inteligente',
'Convocatoria para soluciones tecnológicas en agricultura de precisión y sostenibilidad rural.',
'2025-07-01',
'2025-10-01',
'Innovaciones tecnológicas, impacto en productividad agrícola, uso eficiente de recursos.',
'Ministerio de Agricultura',
'Acceso a redes rurales, subsidios, mentoría especializada.',
'agricultura@ministerio.com',
'Agricultura, Tecnología');

INSERT INTO EVENTOS (titulo, descripcion, fecha, color, id_convocatoria) VALUES
('Demo Day Innovación',
'Presentación final de startups tecnológicas para inversionistas y aceleradoras.',
'2025-06-15',
'#1e3a8a',
1),

('Taller de Crecimiento Sostenible',
'Capacitación para startups verdes sobre modelos de negocio sostenibles y financiamiento.',
'2025-07-10',
'#10b981',
2),

('Foro de Salud Digital',
'Discusión sobre las últimas tendencias en salud digital y bienestar tecnológico.',
'2025-06-20',
'#dc2626',
3),

('Encuentro de Innovadores Educativos',
'Networking y charlas para startups enfocadas en tecnología educativa.',
'2025-06-25',
'#f59e0b',
4),

('Cumbre de Agricultura Inteligente',
'Evento para conectar innovadores en agricultura con líderes de la industria.',
'2025-08-05',
'#854d0e',
5);


INSERT INTO ETAPAS (titulo, descripcion, fecha_inicio, fecha_fin, id_mentor) VALUES
('Definición de Objetivos',
'Primera fase del proyecto, enfocada en establecer metas claras y medibles para el desarrollo del negocio.',
'2025-05-15',
'2025-06-15',
'0987654321'),

('Desarrollo del Producto',
'Etapa dedicada a la creación y perfeccionamiento del producto mínimo viable (MVP).',
'2025-06-16',
'2025-08-15',
'1234567890'),

('Pruebas de Mercado',
'Validación del producto a través de pruebas con usuarios reales para obtener retroalimentación.',
'2025-08-16',
'2025-09-15',
'0987654321'),

('Estrategia de Escalamiento',
'Desarrollo de estrategias para escalar las operaciones y ampliar la base de clientes.',
'2025-09-16',
'2025-11-15',
'1234567890'),

('Presentación a Inversionistas',
'Preparación de presentaciones y pitches para atraer inversores y socios estratégicos.',
'2025-11-16',
'2025-12-15',
'0987654321');

INSERT INTO EVENTOS (titulo, descripcion, fecha, color, id_convocatoria) VALUES
('Demo Day Innovación',
'Presentación final de startups tecnológicas para inversionistas y aceleradoras.',
'2025-06-15',
'#1e3a8a',
1),

('Taller de Crecimiento Sostenible',
'Capacitación para startups verdes sobre modelos de negocio sostenibles y financiamiento.',
'2025-07-10',
'#10b981',
2),

('Foro de Salud Digital',
'Discusión sobre las últimas tendencias en salud digital y bienestar tecnológico.',
'2025-06-20',
'#dc2626',
3),

('Encuentro de Innovadores Educativos',
'Networking y charlas para startups enfocadas en tecnología educativa.',
'2025-06-25',
'#f59e0b',
4),

('Cumbre de Agricultura Inteligente',
'Evento para conectar innovadores en agricultura con líderes de la industria.',
'2025-08-05',
'#854d0e',
5);


INSERT INTO TAREAS (titulo, descripcion, fecha_entrega, id_etapa) VALUES
('Definir misión y visión',
'Redactar la misión y visión del proyecto para establecer una dirección clara.',
'2025-05-20',
1),

('Investigación de mercado',
'Realizar un análisis del mercado para identificar necesidades y oportunidades.',
'2025-06-10',
1),

('Desarrollo del prototipo',
'Crear el primer prototipo del producto para validar la viabilidad técnica.',
'2025-06-30',
2),

('Pruebas iniciales',
'Realizar pruebas de funcionalidad y rendimiento del producto con un grupo pequeño de usuarios.',
'2025-07-15',
2),

('Análisis de resultados',
'Evaluar los resultados de las pruebas iniciales y ajustar el producto según el feedback.',
'2025-08-01',
3),

('Desarrollo de estrategias de marketing',
'Crear un plan de marketing para atraer clientes y aumentar la visibilidad.',
'2025-09-01',
4),

('Pitch para inversores',
'Preparar una presentación para buscar financiamiento y socios estratégicos.',
'2025-10-01',
5),

('Documentación y reporte final',
'Preparar el informe final del proyecto para presentar a los inversionistas.',
'2025-12-01',
5);


INSERT INTO ENTREGABLES (nombre_archivo, ruta_archivo, estado, id_tarea) VALUES
('MisionVision.pdf', '/uploads/entregables/MisionVision.pdf', 'En revisión', 1),
('AnalisisMercado.xlsx', '/uploads/entregables/AnalisisMercado.xlsx', 'Aprobado', 2),
('PrototipoV1.zip', '/uploads/entregables/PrototipoV1.zip', 'En revisión', 3),
('ResultadosPruebas.pdf', '/uploads/entregables/ResultadosPruebas.pdf', 'Rechazado', 4),
('ReporteFinal.docx', '/uploads/entregables/ReporteFinal.docx', 'Pendiente', 5);
