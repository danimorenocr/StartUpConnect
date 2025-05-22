package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.EmprendedorEntity;

public interface EmprendedorService {

    public List<EmprendedorEntity> findAll();

    public void save(EmprendedorEntity emprendedor);

    public EmprendedorEntity findById(Long id);

    public void deleteById(Long id);
    
    public EmprendedorEntity findByDocumento(Long documento);
}
