package com.usta.startupconnect.models.services;

import java.sql.Date;
import java.util.List;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;

public interface ConvocatoriaService {
    public List<ConvocatoriaEntity> findAll();
    
    public void save(ConvocatoriaEntity convocatoria);
    
    public ConvocatoriaEntity findById(Long id);
    
    public void deleteById(Long id);
    
    public ConvocatoriaEntity actualizar(ConvocatoriaEntity convocatoria);
    
    public List<ConvocatoriaEntity> findByTituloContaining(String titulo);
    
    public List<ConvocatoriaEntity> findByStartup(StartupEntity startup);
    
    public List<ConvocatoriaEntity> findByUsuario(UsuarioEntity usuario);
    
    public List<ConvocatoriaEntity> findByFechaInicioBetween(Date fechaInicio, Date fechaFin);
    
    public List<ConvocatoriaEntity> findByFechaFinBetween(Date fechaInicio, Date fechaFin);
    
    public List<ConvocatoriaEntity> findActiveConvocatorias(Date fechaActual);
    
    public List<ConvocatoriaEntity> findUpcomingConvocatorias(Date fechaActual);
    
    public List<ConvocatoriaEntity> findExpiredConvocatorias(Date fechaActual);
    
    public List<ConvocatoriaEntity> findBySectorObjetivoContaining(String sectorObjetivo);
    
    public List<ConvocatoriaEntity> findByOrganizadorContaining(String organizador);
    
    public Long countByStartup(StartupEntity startup);
    
    public Long countByUsuario(UsuarioEntity usuario);

}
