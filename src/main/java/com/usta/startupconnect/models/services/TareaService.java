package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.TareaEntity;

public interface TareaService {
    public List<TareaEntity> findAll();

    public void save(TareaEntity tarea);

    public TareaEntity findById(Long id);

    public void deleteById(Long id);
    
    public List<TareaEntity> findByEtapa(EtapaEntity etapa);
}
