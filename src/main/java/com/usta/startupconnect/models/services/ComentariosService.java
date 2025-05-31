package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.ComentariosEntity;
import com.usta.startupconnect.entities.StartupEntity;
import java.util.List;

public interface ComentariosService {
    List<ComentariosEntity> findAll();
    
    void save(ComentariosEntity comentario);
    
    ComentariosEntity findById(Long id);
    
    List<ComentariosEntity> findByStartupId(Long startupId);
    
    void deleteById(Long id);
}