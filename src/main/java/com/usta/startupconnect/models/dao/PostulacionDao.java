package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.PostulacionEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.ConvocatoriaEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface PostulacionDao extends CrudRepository<PostulacionEntity, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT p FROM PostulacionEntity p WHERE p.startup = ?1")
    List<PostulacionEntity> findByStartup(StartupEntity startup);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM PostulacionEntity p WHERE p.convocatoria = ?1")
    List<PostulacionEntity> findByConvocatoria(ConvocatoriaEntity convocatoria);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM PostulacionEntity p WHERE p.estado = ?1")
    List<PostulacionEntity> findByEstado(String estado);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM PostulacionEntity p WHERE p.fechaPostulacion BETWEEN ?1 AND ?2")
    List<PostulacionEntity> findByFechaPostulacionBetween(Date fechaInicio, Date fechaFin);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM PostulacionEntity p WHERE p.nombreProyecto LIKE %?1%")
    List<PostulacionEntity> findByNombreProyectoContaining(String nombreProyecto);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM PostulacionEntity p WHERE p.etapaProyecto = ?1")
    List<PostulacionEntity> findByEtapaProyecto(String etapaProyecto);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM PostulacionEntity p WHERE p.numeroIntegrantes >= ?1")
    List<PostulacionEntity> findByNumeroIntegrantesGreaterThanEqual(Short numeroIntegrantes);

}
