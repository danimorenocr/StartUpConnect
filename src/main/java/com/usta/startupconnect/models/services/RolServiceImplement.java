package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.RolEntity;
import com.usta.startupconnect.models.dao.RolDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolServiceImplement implements RolService {
    @Autowired
    private RolDao rolDAO;

    @Override
    @Transactional
    public List<RolEntity> findAll() {
        return (List<RolEntity>) rolDAO.findAll();
    }

    @Override
    @Transactional
    public void save(RolEntity rol) {
        rolDAO.save(rol);
    }

    @Override
    @Transactional
    public RolEntity findById(Long id) {
        return rolDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deletebyId(Long id) {
        rolDAO.deleteById(id);
    }

    @Override
    @Transactional
    public RolEntity actualizar(RolEntity rol) {
        return rolDAO.save(rol);
    }
}
