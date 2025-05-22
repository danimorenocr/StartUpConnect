package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.EntregableEntity;
import com.usta.startupconnect.entities.TareaEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EntregablesDao extends CrudRepository<EntregableEntity, Long> {


    @Transactional
    @Modifying
    @Query("UPDATE EntregableEntity e SET e.estado = ?2, e.nombreArchivo = ?3 WHERE e.id = ?1")
    public int actualizarCalificacion(Long idEntregable, Integer calificacion, String comentario);

    @Transactional
    @Modifying
    @Query("UPDATE EntregableEntity e SET e.rutaArchivo = ?2 WHERE e.id = ?1")
    public int actualizarArchivo(Long idEntregable, String urlArchivo);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM EntregableEntity e WHERE e.tarea = ?1")
    List<EntregableEntity> findByTarea(TareaEntity tarea);
}
