package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.CalendarioEntity;
import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.models.dao.CalendarioDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CalendarioServiceImplement implements CalendarioService {
    @Autowired
    private CalendarioDao calendarioDAO;

    @Override
    @Transactional
    public List<CalendarioEntity> findAll() {
        return (List<CalendarioEntity>) calendarioDAO.findAll();
    }

    @Override
    @Transactional
    public void save(CalendarioEntity calendario) {
        calendarioDAO.save(calendario);
    }

    @Override
    @Transactional
    public CalendarioEntity findById(Long id) {
        return calendarioDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        calendarioDAO.deleteById(id);
    }

    @Override
    @Transactional
    public CalendarioEntity actualizar(CalendarioEntity calendario) {
        return calendarioDAO.save(calendario);
    }

    @Override
    @Transactional
    public CalendarioEntity findByConvocatoria(ConvocatoriaEntity convocatoria) {
        return calendarioDAO.findByConvocatoria(convocatoria);
    }
    
    @Override
    @Transactional
    public List<CalendarioEntity> findByNombreEventoContaining(String nombreEvento) {
        return calendarioDAO.findEventosByNombre(nombreEvento);
    }
    
    @Override
    @Transactional
    public List<CalendarioEntity> findByFechaInicioAfter(Date fecha) {
        // Implementar en el DAO o usar una solución temporal
        return calendarioDAO.findEventosFuturos();
    }
    
    @Override
    @Transactional
    public List<CalendarioEntity> findByFechaFinBefore(Date fecha) {
        // Este método no está implementado directamente en el DAO
        // Solución temporal: devolver null o lista vacía
        return null;
    }
    
    @Override
    @Transactional
    public List<CalendarioEntity> findByFechaInicioBetween(Date fechaInicio, Date fechaFin) {
        return calendarioDAO.findEventosByRangoDeFechas(fechaInicio, fechaFin);
    }
    
    @Override
    @Transactional
    public List<CalendarioEntity> findActiveCalendarios(Date fechaActual) {
        // Este método no está implementado directamente en el DAO
        // Solución temporal: devolver null o lista vacía
        return null;
    }
    
    @Override
    @Transactional
    public List<CalendarioEntity> findUpcomingCalendarios(Date fechaActual) {
        return calendarioDAO.findEventosFuturos();
    }
    
    @Override
    @Transactional
    public List<CalendarioEntity> findPastCalendarios(Date fechaActual) {
        // Este método no está implementado directamente en el DAO
        // Solución temporal: devolver null o lista vacía
        return null;
    }
    
    @Override
    @Transactional
    public boolean isFechaDisponible(Date fechaInicio, Date fechaFin) {
        // Implementación básica: verificar si no hay eventos en ese rango de fechas
        List<CalendarioEntity> eventosSolapados = calendarioDAO.findEventosByRangoDeFechas(fechaInicio, fechaFin);
        return eventosSolapados == null || eventosSolapados.isEmpty();
    }
}