package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.models.dao.StartupDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StartupServiceImplement implements StartupService {
    @Autowired
    private StartupDao startupDao;

    @Override
    @Transactional(readOnly = true)
    public List<StartupEntity> findAll() {
        return (List<StartupEntity>) startupDao.findAll();
    }
}