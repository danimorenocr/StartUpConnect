package com.usta.startupconnect.models.services;

import java.util.Date;
import java.util.List;

import com.usta.startupconnect.entities.CalendarioEntity;
import com.usta.startupconnect.entities.ConvocatoriaEntity;

public interface CalendarioService {
    public List<CalendarioEntity> findAll();
    
    public void save(CalendarioEntity calendario);
    
    public CalendarioEntity findById(Long id);
    
    public void deleteById(Long id);
    
    public CalendarioEntity actualizar(CalendarioEntity calendario);
    
    public CalendarioEntity findByConvocatoria(ConvocatoriaEntity convocatoria);
    
    public List<CalendarioEntity> findByNombreEventoContaining(String nombreEvento);
    
    public List<CalendarioEntity> findByFechaInicioAfter(Date fecha);
    
    public List<CalendarioEntity> findByFechaFinBefore(Date fecha);
    
    public List<CalendarioEntity> findByFechaInicioBetween(Date fechaInicio, Date fechaFin);
    
    public List<CalendarioEntity> findActiveCalendarios(Date fechaActual);
    
    public List<CalendarioEntity> findUpcomingCalendarios(Date fechaActual);
    
    public List<CalendarioEntity> findPastCalendarios(Date fechaActual);
    
    public boolean isFechaDisponible(Date fechaInicio, Date fechaFin);

}
