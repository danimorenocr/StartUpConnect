package com.usta.startupconnect.models.services;

import java.util.Date;
import java.util.List;

import com.usta.startupconnect.entities.CalendarioEntity;
import com.usta.startupconnect.entities.NotificacionEntity;

public interface NotificacionService {
    public List<NotificacionEntity> findAll();
    
    public void save(NotificacionEntity notificacion);
    
    public NotificacionEntity findById(Long id);
    
    public void deleteById(Long id);
    
    public NotificacionEntity actualizar(NotificacionEntity notificacion);
    
    public List<NotificacionEntity> findByMensajeContaining(String mensaje);
    
    public List<NotificacionEntity> findByTipo(String tipo);
    
    public List<NotificacionEntity> findByFecha(Date fecha);
    
    public List<NotificacionEntity> findByFechaBetween(Date fechaInicio, Date fechaFin);
    
    public List<NotificacionEntity> findByCalendario(CalendarioEntity calendario);
    
    public List<NotificacionEntity> findRecentNotificaciones(Date fechaReciente);
    
    public Long countByTipo(String tipo);
    
    public Long countByFechaBetween(Date fechaInicio, Date fechaFin);
    
}
