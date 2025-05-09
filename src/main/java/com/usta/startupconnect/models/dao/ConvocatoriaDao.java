package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ConvocatoriaDao extends CrudRepository<ConvocatoriaEntity, Long> {

    @Transactional
    @Query("SELECT c FROM ConvocatoriaEntity c")
    public List<ConvocatoriaEntity> findAllConvocatorias();
    
    @Transactional
    @Query("SELECT c FROM ConvocatoriaEntity c WHERE c.fechaInicioConvocatoria <= ?1 AND c.fechaFinConvocatoria >= ?1")
    public List<ConvocatoriaEntity> findConvocatoriasActivas(Date fechaActual);
    
    @Transactional
    @Query("SELECT c FROM ConvocatoriaEntity c WHERE c.tituloConvocatoria LIKE %?1%")
    public List<ConvocatoriaEntity> findByTitulo(String titulo);
    
    @Transactional
    @Query("SELECT c FROM ConvocatoriaEntity c WHERE c.fechaFinConvocatoria < ?1")
    public List<ConvocatoriaEntity> findConvocatoriasCerradas(Date fechaActual);
    
    @Transactional
    @Modifying
    @Query("UPDATE ConvocatoriaEntity c SET c.estadoConvocatoria = ?2 WHERE c.idConvocatoria = ?1")
    public int actualizarEstadoConvocatoria(Long idConvocatoria, boolean estado);
    
    @Transactional
    @Modifying
    @Query("UPDATE ConvocatoriaEntity c SET c.tituloConvocatoria = ?2, c.descripcionConvocatoria = ?3, c.fechaInicioConvocatoria = ?4, c.fechaFinConvocatoria = ?5 WHERE c.idConvocatoria = ?1")
    public int actualizarDatosConvocatoria(Long idConvocatoria, String titulo, String descripcion, Date fechaInicio, Date fechaFin);
}
