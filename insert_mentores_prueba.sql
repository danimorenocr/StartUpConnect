-- Script para verificar e insertar mentores de prueba
-- Verificar si existen mentores
SELECT COUNT(*) as total_mentores FROM mentores;

-- Verificar mentores existentes
SELECT m.documento, u.nombre as nombre_usuario, m.especialidad, u.email 
FROM mentores m 
JOIN usuarios u ON m.documento = u.documento;

-- Si no hay mentores, insertar algunos de prueba
-- Primero insertamos usuarios con rol de mentor (id_rol = 3 para mentor)

-- Insertar usuarios mentores si no existen
INSERT INTO usuarios (documento, nombre, email, contrasenna, fecha_creacion, telefono, foto_url, id_rol) 
VALUES 
('12345678', 'Carlos Méndez', 'carlos.mendez@startup.com', '$2a$10$example', '2024-01-15', '3001234567', 'default-avatar.jpg', 3),
('87654321', 'Ana García', 'ana.garcia@startup.com', '$2a$10$example', '2024-01-15', '3009876543', 'default-avatar.jpg', 3),
('11223344', 'Roberto Silva', 'roberto.silva@startup.com', '$2a$10$example', '2024-01-15', '3001122334', 'default-avatar.jpg', 3)
ON CONFLICT (documento) DO NOTHING;

-- Insertar mentores correspondientes
INSERT INTO mentores (documento, especialidad, biografia, linkedin, anos_experiencia, id_usuario) 
VALUES 
('12345678', 'Tecnología e Innovación', 'Experto en desarrollo de software y startups tecnológicas con más de 10 años de experiencia.', 'https://linkedin.com/in/carlosmendez', 10, '12345678'),
('87654321', 'Marketing Digital', 'Especialista en marketing digital y crecimiento de startups con experiencia en múltiples sectores.', 'https://linkedin.com/in/anagarcia', 8, '87654321'),
('11223344', 'Finanzas y Negocios', 'Consultor financiero especializado en startups y modelos de negocio escalables.', 'https://linkedin.com/in/robertosilva', 12, '11223344')
ON CONFLICT (documento) DO NOTHING;

-- Verificar inserción
SELECT m.documento, u.nombre as nombre_usuario, m.especialidad, u.email 
FROM mentores m 
JOIN usuarios u ON m.documento = u.documento;
