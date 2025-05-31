package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.PostulacionEntity;

public interface PostulacionService {
    
    public List<PostulacionEntity> findAll();
    
    public List<PostulacionEntity> findByConvocatoria(ConvocatoriaEntity convocatoria);

    public void save(PostulacionEntity postulacion);

    public PostulacionEntity findById(Long id);

    public void deleteById(Long id);

}
