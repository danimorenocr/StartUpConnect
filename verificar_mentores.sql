-- Script para verificar y corregir los datos de mentores
-- Verificar mentores existentes y su relación con usuarios
SELECT 
    m.documento as mentor_doc,
    m.especialidad,
    m.id_usuario,
    u.documento as usuario_doc,
    u.nombre as nombre_usuario,
    u.email as email_usuario
FROM mentores m 
LEFT JOIN usuarios u ON m.id_usuario = u.documento
ORDER BY m.documento;

-- Verificar mentores sin usuario asociado
SELECT 
    m.documento, 
    m.especialidad, 
    m.id_usuario,
    'SIN USUARIO' as estado
FROM mentores m 
LEFT JOIN usuarios u ON m.id_usuario = u.documento
WHERE u.documento IS NULL;

-- Corregir los mentores existentes para que apunten a sus propios documentos como usuarios
UPDATE mentores 
SET id_usuario = documento 
WHERE documento IN ('1111', '1234');

-- Verificar después de la corrección
SELECT 
    m.documento as mentor_doc,
    m.especialidad,
    m.id_usuario,
    u.documento as usuario_doc,
    u.nombre as nombre_usuario,
    u.email as email_usuario
FROM mentores m 
LEFT JOIN usuarios u ON m.id_usuario = u.documento
ORDER BY m.documento;
