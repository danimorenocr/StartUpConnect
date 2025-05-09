package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.models.dao.EtapaDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

@Service
public class EtapaServiceImplement implements EtapaService {
    @Autowired
    private EtapaDao etapaDAO;

    @Override
    @Transactional
    public List<EtapaEntity> findAll() {
        return (List<EtapaEntity>) etapaDAO.findAll();
    }

    @Override
    @Transactional
    public void save(EtapaEntity etapa) {
        etapaDAO.save(etapa);
    }

    @Override
    @Transactional
    public EtapaEntity findById(Long id) {
        return etapaDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        etapaDAO.deleteById(id);
    }

    @Override
    @Transactional
    public EtapaEntity actualizar(EtapaEntity etapa) {
        return etapaDAO.save(etapa);
    }
    
    @Override
    @Transactional
    public List<EtapaEntity> findByTituloContaining(String titulo) {
        // Usar método similar disponible en el DAO
        return etapaDAO.findByNombre(titulo);
    }
    
    @Override
    @Transactional
    public List<EtapaEntity> findByMentor(MentorEntity mentor) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<EtapaEntity> findByFechaInicioAfter(Date fecha) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<EtapaEntity> findByFechaFinBefore(Date fecha) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<EtapaEntity> findByFechaInicioBetween(Date fechaInicio, Date fechaFin) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<EtapaEntity> findCurrentEtapas(Date fechaActual) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<EtapaEntity> findUpcomingEtapas(Date fechaActual) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<EtapaEntity> findPastEtapas(Date fechaActual) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
}