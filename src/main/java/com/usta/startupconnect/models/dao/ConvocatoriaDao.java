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

    @Transactional(readOnly = true)
    @Query("SELECT c FROM ConvocatoriaEntity c WHERE c.fechaInicio <= ?1 AND c.fechaFin >= ?1")
    public List<ConvocatoriaEntity> findConvocatoriasActivas(Date fecha);

    @Transactional
    @Query("SELECT c FROM ConvocatoriaEntity c WHERE c.titulo LIKE %?1%")
    public List<ConvocatoriaEntity> findByTitulo(String titulo);

    @Transactional(readOnly = true)
    @Query("SELECT c FROM ConvocatoriaEntity c WHERE c.fechaFin < ?1")
    public List<ConvocatoriaEntity> findConvocatoriasCerradas(Date fechaActual);

    @Transactional(readOnly = true)
    @Query("SELECT c FROM ConvocatoriaEntity c WHERE c.sectorObjetivo = ?1")
    List<ConvocatoriaEntity> findBySectorObjetivo(String sector);

    @Transactional(readOnly = true)
    @Query("SELECT c FROM ConvocatoriaEntity c WHERE c.organizador = ?1")
    List<ConvocatoriaEntity> findByOrganizador(String organizador);

    @Transactional(readOnly = true)
    @Query("SELECT c FROM ConvocatoriaEntity c WHERE c.fechaFin < ?1")
    List<ConvocatoriaEntity> findConvocatoriasVencidas(Date fecha);

    @Transactional
    @Modifying
    @Query("UPDATE ConvocatoriaEntity c SET c.titulo = ?2, c.descripcion = ?3, c.fechaInicio = ?4, c.fechaFin = ?5 WHERE c.id = ?1")
    public int actualizarDatosConvocatoria(Long idConvocatoria, String titulo, String descripcion, Date fechaInicio, Date fechaFin);
}
