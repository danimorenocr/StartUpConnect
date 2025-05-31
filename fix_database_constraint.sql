-- Script para eliminar la restricción de unicidad incorrecta en la tabla postulaciones
-- Esta restricción impide que múltiples startups se postulen a la misma convocatoria

-- Eliminar la restricción de unicidad en id_convocatoria
ALTER TABLE postulaciones DROP CONSTRAINT IF EXISTS postulaciones_id_convocatoria_key;

-- Verificar que no hay otras restricciones similares
-- Si hay más restricciones de unicidad relacionadas, también las eliminamos
ALTER TABLE postulaciones DROP CONSTRAINT IF EXISTS uk_postulaciones_id_convocatoria;
ALTER TABLE postulaciones DROP CONSTRAINT IF EXISTS postulaciones_convocatoria_unique;

-- Comentario: La relación correcta es ManyToOne, donde:
-- - Múltiples postulaciones (Many) pueden referirse a una convocatoria (One)
-- - Una convocatoria puede tener múltiples postulaciones
-- - La restricción de unicidad era incorrecta para este caso de uso
