package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.models.dao.StartupDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StartupServiceImplement implements StartupService {
    @Autowired
    private StartupDao startupDAO;

    @Override
    @Transactional
    public List<StartupEntity> findAll() {
        return (List<StartupEntity>) startupDAO.findAll();
    }

    @Override
    @Transactional
    public void save(StartupEntity startup) {
        startupDAO.save(startup);
    }

    @Override
    @Transactional
    public StartupEntity findById(Long id) {
        return startupDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        startupDAO.deleteById(id);
    }

    @Override
    @Transactional
    public StartupEntity actualizar(StartupEntity startup) {
        return startupDAO.save(startup);
    }
    
    @Override
    @Transactional
    public List<StartupEntity> findByEmprendedor(EmprendedorEntity emprendedor) {
        // Usar el documento del emprendedor que es el ID en la entidad EmprendedorEntity
        return startupDAO.findByEmprendedorId(Long.valueOf(emprendedor.getDocumento()));
    }
    
    @Override
    @Transactional
    public List<StartupEntity> findBySector(String sector) {
        return startupDAO.findBySector(sector);
    }
    
    @Override
    @Transactional
    public List<StartupEntity> findByEstado(String estado) {
        // Este método no está directamente implementado en StartupDao
        // Necesitamos agregar la consulta en el DAO o filtrar manualmente
        return null; // Reemplazar con implementación adecuada
    }
    
    @Override
    @Transactional
    public List<StartupEntity> findByNombreStartupContaining(String nombre) {
        return startupDAO.findByNombre(nombre);
    }
    
    @Override
    @Transactional
    public void incrementarLikes(Long id) {
        StartupEntity startup = findById(id);
        if (startup != null) {
            // Usar cantLikes que es el nombre del campo en StartupEntity
            startup.setCantLikes(startup.getCantLikes() + 1);
            startupDAO.save(startup);
        }
    }
}