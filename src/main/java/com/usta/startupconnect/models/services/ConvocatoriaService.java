package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.ConvocatoriaEntity;

public interface ConvocatoriaService {
    public List<ConvocatoriaEntity> findAll();

    public void save(ConvocatoriaEntity convocatoria);

    public ConvocatoriaEntity findById(Long id);

    public void deleteById(Long id);

}
