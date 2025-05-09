package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.EventoEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.models.dao.EventoDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
public class EventoServiceImplement implements EventoService {
    @Autowired
    private EventoDao eventoDAO;

    @Override
    @Transactional
    public List<EventoEntity> findAll() {
        return (List<EventoEntity>) eventoDAO.findAll();
    }

    @Override
    @Transactional
    public void save(EventoEntity evento) {
        eventoDAO.save(evento);
    }

    @Override
    @Transactional
    public EventoEntity findById(Long id) {
        return eventoDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        eventoDAO.deleteById(id);
    }

    @Override
    @Transactional
    public EventoEntity actualizar(EventoEntity evento) {
        return eventoDAO.save(evento);
    }
    
    @Override
    @Transactional
    public List<EventoEntity> findByStartup(StartupEntity startup) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<EventoEntity> findByEmprendedor(EmprendedorEntity emprendedor) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public List<EventoEntity> findByMentor(MentorEntity mentor) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver lista vacía
        return new ArrayList<>();
    }
}