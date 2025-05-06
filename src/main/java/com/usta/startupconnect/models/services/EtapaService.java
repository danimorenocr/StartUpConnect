package com.usta.startupconnect.models.services;

import java.sql.Date;
import java.util.List;

import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.MentorEntity;

public interface EtapaService {
    public List<EtapaEntity> findAll();
    
    public void save(EtapaEntity etapa);
    
    public EtapaEntity findById(Long id);
    
    public void deleteById(Long id);
    
    public EtapaEntity actualizar(EtapaEntity etapa);
    
    public List<EtapaEntity> findByTituloContaining(String titulo);
    
    public List<EtapaEntity> findByMentor(MentorEntity mentor);
    
    public List<EtapaEntity> findByFechaInicioAfter(Date fecha);
    
    public List<EtapaEntity> findByFechaFinBefore(Date fecha);
    
    public List<EtapaEntity> findByFechaInicioBetween(Date fechaInicio, Date fechaFin);
    
    public List<EtapaEntity> findCurrentEtapas(Date fechaActual);
    
    public List<EtapaEntity> findUpcomingEtapas(Date fechaActual);
    
    public List<EtapaEntity> findPastEtapas(Date fechaActual);

}
