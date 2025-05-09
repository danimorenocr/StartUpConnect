package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.dao.ConvocatoriaDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

@Service
public class ConvocatoriaServiceImplement implements ConvocatoriaService {
    @Autowired
    private ConvocatoriaDao convocatoriaDAO;

    @Override
    @Transactional
    public List<ConvocatoriaEntity> findAll() {
        return (List<ConvocatoriaEntity>) convocatoriaDAO.findAll();
    }

    @Override
    @Transactional
    public void save(ConvocatoriaEntity convocatoria) {
        convocatoriaDAO.save(convocatoria);
    }

    @Override
    @Transactional
    public ConvocatoriaEntity findById(Long id) {
        return convocatoriaDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        convocatoriaDAO.deleteById(id);
    }

    @Override
    @Transactional
    public ConvocatoriaEntity actualizar(ConvocatoriaEntity convocatoria) {
        return convocatoriaDAO.save(convocatoria);
    }
    
    @Override
    @Transactional
    public List<ConvocatoriaEntity> findByTituloContaining(String titulo) {
        return convocatoriaDAO.findByTitulo(titulo);
    }
    
    @Override
    @Transactional
    public List<ConvocatoriaEntity> findByStartup(StartupEntity startup) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<ConvocatoriaEntity> findByUsuario(UsuarioEntity usuario) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<ConvocatoriaEntity> findByFechaInicioBetween(Date fechaInicio, Date fechaFin) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<ConvocatoriaEntity> findByFechaFinBetween(Date fechaInicio, Date fechaFin) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<ConvocatoriaEntity> findActiveConvocatorias(Date fechaActual) {
        return convocatoriaDAO.findConvocatoriasActivas(fechaActual);
    }
    
    @Override
    @Transactional
    public List<ConvocatoriaEntity> findUpcomingConvocatorias(Date fechaActual) {
        // Este método no está disponible exactamente en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<ConvocatoriaEntity> findExpiredConvocatorias(Date fechaActual) {
        return convocatoriaDAO.findConvocatoriasCerradas(fechaActual);
    }
    
    @Override
    @Transactional
    public List<ConvocatoriaEntity> findBySectorObjetivoContaining(String sectorObjetivo) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<ConvocatoriaEntity> findByOrganizadorContaining(String organizador) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public Long countByStartup(StartupEntity startup) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver 0
        return 0L;
    }
    
    @Override
    @Transactional
    public Long countByUsuario(UsuarioEntity usuario) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver 0
        return 0L;
    }
}