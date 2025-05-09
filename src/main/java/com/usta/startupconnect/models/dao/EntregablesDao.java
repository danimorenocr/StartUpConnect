package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.EntregableEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface EntregablesDao extends CrudRepository<EntregableEntity, Long> {

    @Transactional
    @Query("SELECT e FROM EntregableEntity e WHERE e.tarea.idTarea = ?1")
    public List<EntregableEntity> findByTareaId(Long idTarea);
    
    @Transactional
    @Query("SELECT e FROM EntregableEntity e WHERE e.startup.idStartup = ?1")
    public List<EntregableEntity> findByStartupId(Long idStartup);
    
    @Transactional
    @Query("SELECT e FROM EntregableEntity e WHERE e.fechaEntrega BETWEEN ?1 AND ?2")
    public List<EntregableEntity> findByFechaBetween(Date fechaInicio, Date fechaFin);
    
    @Transactional
    @Query("SELECT e FROM EntregableEntity e WHERE e.calificacionEntregable >= ?1")
    public List<EntregableEntity> findByCalificacionMinima(int calificacionMinima);
    
    @Transactional
    @Modifying
    @Query("UPDATE EntregableEntity e SET e.calificacionEntregable = ?2, e.comentarioEntregable = ?3 WHERE e.idEntregable = ?1")
    public int actualizarCalificacion(Long idEntregable, Integer calificacion, String comentario);
    
    @Transactional
    @Modifying
    @Query("UPDATE EntregableEntity e SET e.urlArchivo = ?2 WHERE e.idEntregable = ?1")
    public int actualizarArchivo(Long idEntregable, String urlArchivo);
}
