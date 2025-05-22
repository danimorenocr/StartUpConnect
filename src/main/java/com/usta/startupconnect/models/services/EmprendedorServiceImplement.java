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
    @Transactional
    public List<EmprendedorEntity> findAll() {
        return (List<EmprendedorEntity>) emprendedorDao.findAll();
    }

    @Override
    @jakarta.transaction.Transactional
    public void save(EmprendedorEntity usuario) {
        emprendedorDao.save(usuario);
    }

    @Override
    @Transactional
    public EmprendedorEntity findById(Long id) {
        return emprendedorDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        emprendedorDao.deleteById(id);
    }

    @Override
    @Transactional
    public EmprendedorEntity findByDocumento(Long documento) {
        return emprendedorDao.findByDocumento(documento);
    }
}