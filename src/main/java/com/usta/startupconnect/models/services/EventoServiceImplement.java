package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.EventoEntity;
import com.usta.startupconnect.models.dao.EventoDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventoServiceImplement implements EventoService {
    @Autowired
    private EventoDao eventoDao;

    @Override
    @Transactional(readOnly = true)
    public List<EventoEntity> findAll() {
        return (List<EventoEntity>) eventoDao.findAll();
    }

    @Override
    @jakarta.transaction.Transactional
    public void save(EventoEntity usuario) {
        eventoDao.save(usuario);
    }

    @Override
    @Transactional
    public EventoEntity findById(Long id) {
        return eventoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        eventoDao.deleteById(id);
    }
}