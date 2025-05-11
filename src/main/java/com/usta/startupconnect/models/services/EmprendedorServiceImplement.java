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
    private EmprendedorDao emprendedorDAO;

    @Override
    @Transactional
    public List<EmprendedorEntity> findAll() {
        return (List<EmprendedorEntity>) emprendedorDAO.findAll();
    }
}