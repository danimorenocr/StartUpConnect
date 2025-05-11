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
    private RolDao rolDao;

    @Override
    @Transactional(readOnly = true)
    public List<RolEntity> findAll() {
        return (List<RolEntity>) rolDao.findAll();
    }
}