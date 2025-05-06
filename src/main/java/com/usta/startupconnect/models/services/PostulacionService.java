package com.usta.startupconnect.models.services;

import java.util.Date;
import java.util.List;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.PostulacionEntity;
import com.usta.startupconnect.entities.StartupEntity;

public interface PostulacionService {
    public List<PostulacionEntity> findAll();
    
    public void save(PostulacionEntity postulacion);
    
    public PostulacionEntity findById(Long id);
    
    public void deleteById(Long id);
    
    public PostulacionEntity actualizar(PostulacionEntity postulacion);
    
    public List<PostulacionEntity> findByStartup(StartupEntity startup);
    
    public List<PostulacionEntity> findByConvocatoria(ConvocatoriaEntity convocatoria);
    
    public List<PostulacionEntity> findByEstado(String estado);
    
    public List<PostulacionEntity> findByNombreProyectoContaining(String nombre);
    
    public List<PostulacionEntity> findByEtapaProyecto(String etapa);
    
    public List<PostulacionEntity> findByFechaPostulacionBetween(Date fechaInicio, Date fechaFin);
    
    public Long countByConvocatoria(ConvocatoriaEntity convocatoria);
    
    public Long countByStartup(StartupEntity startup);

}
