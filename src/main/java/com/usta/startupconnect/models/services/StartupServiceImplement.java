package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.models.dao.StartupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StartupServiceImplement implements StartupService {
    @Autowired
    private StartupDao startupDao;

    @Override
    @Transactional(readOnly = true)
    public List<StartupEntity> findAll() {
        return (List<StartupEntity>) startupDao.findAll();
    }

        @Override
    @jakarta.transaction.Transactional
    public void save(StartupEntity startup) {
        startupDao.save(startup);
    }

    @Override
    @Transactional
    public StartupEntity findById(Long id) {
        return startupDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        startupDao.deleteById(id);
    }

}