package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.models.dao.StartupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StartupServiceImplement implements StartupService {
    @Autowired
    private StartupDao startupDao;

    @Override
    @Transactional(readOnly = true)
    public List<StartupEntity> findAllActive() {
        return ((List<StartupEntity>) startupDao.findAll())
                .stream()
                .filter(startup -> startup.getEstado() != null)
                .sorted(Comparator.comparing(StartupEntity::getFechaCreacion).reversed())
                .collect(Collectors.toList());
    }

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
    
    @Override
    @Transactional(readOnly = true)
    public List<StartupEntity> findByEmprendedor(EmprendedorEntity emprendedor) {
        return startupDao.findByEmprendedor(emprendedor);
    }

}