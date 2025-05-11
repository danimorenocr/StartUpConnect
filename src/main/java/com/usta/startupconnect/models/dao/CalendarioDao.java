package com.usta.startupconnect.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.usta.startupconnect.entities.CalendarioEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.entities.ConvocatoriaEntity;

import java.util.Date;
import java.util.List;

public interface CalendarioDao extends CrudRepository<CalendarioEntity, Long> {
    @Transactional
    @Query("SELECT US FROM UsuarioEntity US WHERE US.emailUsu = ?1")
    public UsuarioEntity findByemail(String email);
    
    @Transactional(readOnly = true)
    @Query("SELECT c FROM CalendarioEntity c WHERE c.fechaInicio >= ?1 AND c.fechaFin <= ?2")
    public List<CalendarioEntity> findEventosByRangoDeFechas(Date fechaInicio, Date fechaFin);
    
    @Transactional(readOnly = true)
    @Query("SELECT c FROM CalendarioEntity c WHERE c.nombreEvento LIKE %?1%")
    public List<CalendarioEntity> findEventosByNombre(String nombreEvento);
    
    @Transactional(readOnly = true)
    @Query("SELECT c FROM CalendarioEntity c WHERE c.convocatoria = ?1")
    public List<CalendarioEntity> findByConvocatoria(ConvocatoriaEntity convocatoria);
    
    @Transactional(readOnly = true)
    @Query("SELECT c FROM CalendarioEntity c WHERE c.fechaInicio >= CURRENT_DATE ORDER BY c.fechaInicio ASC")
    public List<CalendarioEntity> findEventosFuturos();

    @Transactional(readOnly = true)
    @Query("SELECT c FROM CalendarioEntity c WHERE c.fechaInicio <= ?1 AND c.fechaFin >= ?1")
    List<CalendarioEntity> findEventosActivos(Date fecha);

    @Transactional(readOnly = true)
    @Query("SELECT c FROM CalendarioEntity c WHERE c.fechaInicio BETWEEN ?1 AND ?2")
    List<CalendarioEntity> findByFechaInicioBetween(Date fechaInicio, Date fechaFin);

    @Transactional(readOnly = true)
    @Query("SELECT c FROM CalendarioEntity c WHERE c.nombreEvento LIKE %?1%")
    List<CalendarioEntity> findByNombreEventoContaining(String nombre);
}
