package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.RolEntity;

public interface RolService {
    
    public List<RolEntity> findAll();

    public void save(RolEntity rol);

    public RolEntity findById(Long id);

    public void deletebyId(Long id);

    public RolEntity actualizar(RolEntity rol);
}
