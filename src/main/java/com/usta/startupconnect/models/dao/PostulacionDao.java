package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.PostulacionEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface PostulacionDao extends CrudRepository<PostulacionEntity, Long> {

    @Transactional
    @Query("SELECT p FROM PostulacionEntity p WHERE p.convocatoria.idConvocatoria = ?1")
    public List<PostulacionEntity> findByConvocatoriaId(Long idConvocatoria);
    
    @Transactional
    @Query("SELECT p FROM PostulacionEntity p WHERE p.startup.idStartup = ?1")
    public List<PostulacionEntity> findByStartupId(Long idStartup);
    
    @Transactional
    @Query("SELECT p FROM PostulacionEntity p WHERE p.estadoPostulacion = ?1")
    public List<PostulacionEntity> findByEstado(String estado);
    
    @Transactional
    @Query("SELECT p FROM PostulacionEntity p WHERE p.fechaPostulacion BETWEEN ?1 AND ?2")
    public List<PostulacionEntity> findByFechaBetween(Date fechaInicio, Date fechaFin);
    
    @Transactional
    @Modifying
    @Query("UPDATE PostulacionEntity p SET p.estadoPostulacion = ?2 WHERE p.idPostulacion = ?1")
    public int actualizarEstadoPostulacion(Long idPostulacion, String estado);
    
    @Transactional
    @Modifying
    @Query("UPDATE PostulacionEntity p SET p.observacionesPostulacion = ?2 WHERE p.idPostulacion = ?1")
    public int actualizarObservaciones(Long idPostulacion, String observaciones);
}
