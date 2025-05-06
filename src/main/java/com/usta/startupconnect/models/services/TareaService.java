package com.usta.startupconnect.models.services;

import java.sql.Date;
import java.util.List;

import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.TareaEntity;

public interface TareaService {
    public List<TareaEntity> findAll();
    
    public void save(TareaEntity tarea);
    
    public TareaEntity findById(Long id);
    
    public void deleteById(Long id);
    
    public TareaEntity actualizar(TareaEntity tarea);
    
    public List<TareaEntity> findByTituloContaining(String titulo);
    
    public List<TareaEntity> findByEtapa(EtapaEntity etapa);
    
    public List<TareaEntity> findByFechaEntregaBefore(Date fecha);
    
    public List<TareaEntity> findByFechaEntregaAfter(Date fecha);
    
    public List<TareaEntity> findByFechaEntregaBetween(Date fechaInicio, Date fechaFin);
    
    public List<TareaEntity> findPendingTareas(Date fechaActual);
    
    public List<TareaEntity> findCompletedTareas(Date fechaActual);
    
    public Long countByEtapa(EtapaEntity etapa);
}
