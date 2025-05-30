package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.EmprendedorEntity;

public interface EmprendedorService {

    public List<EmprendedorEntity> findAll();

    public void save(EmprendedorEntity emprendedor);

    public EmprendedorEntity findById(String id);    public void deleteById(String id);
    
    public EmprendedorEntity findByDocumento(String documento);
    
    public EmprendedorEntity findByUsuarioEmail(String email);
}
