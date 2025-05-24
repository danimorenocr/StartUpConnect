package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.StartupEntity;

public interface StartupService {
    
    public List<StartupEntity> findAll();

    public void save(StartupEntity startup);

    public StartupEntity findById(Long id);

    public void deleteById(Long id);
    
    public List<StartupEntity> findByEmprendedor(EmprendedorEntity emprendedor);

}
