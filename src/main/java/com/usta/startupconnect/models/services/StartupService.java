package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.StartupEntity;

public interface StartupService {
    
    public List<StartupEntity> findAll();

    public void save(StartupEntity startup);

    public StartupEntity findById(Long id);

    public void deleteById(Long id);

    public StartupEntity actualizar(StartupEntity startup);

    public List<StartupEntity> findByEmprendedor(EmprendedorEntity emprendedor);

    public List<StartupEntity> findBySector(String sector);

    public List<StartupEntity> findByEstado(String estado);

    public List<StartupEntity> findByNombreStartupContaining(String nombre);

    public void incrementarLikes(Long id);
}
