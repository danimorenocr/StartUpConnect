package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.models.dao.EmprendedorDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmprendedorServiceImplement implements EmprendedorService {
    @Autowired
    private EmprendedorDao emprendedorDao;

    @Override
    @Transactional(readOnly = true)
    public List<EmprendedorEntity> findAll() {
        return (List<EmprendedorEntity>) emprendedorDao.findAll();
    }

    @Override
    @Transactional
    public void save(EmprendedorEntity usuario) {
        emprendedorDao.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public EmprendedorEntity findById(String id) {
        return emprendedorDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        emprendedorDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public EmprendedorEntity findByDocumento(String documento) {
        return emprendedorDao.findById(documento).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmprendedorEntity findByUsuarioEmail(String email) {
        return emprendedorDao.findByUsuarioEmail(email);
    }
}