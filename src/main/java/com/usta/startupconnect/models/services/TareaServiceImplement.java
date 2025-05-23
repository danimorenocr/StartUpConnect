package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.TareaEntity;
import com.usta.startupconnect.models.dao.TareaDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TareaServiceImplement implements TareaService {
    @Autowired
    private TareaDao tareaDao;

    @Override
    @Transactional
    public List<TareaEntity> findAll() {
        return (List<TareaEntity>) tareaDao.findAll();
    }

    @Override
    @jakarta.transaction.Transactional
    public void save(TareaEntity usuario) {
        tareaDao.save(usuario);
    }

    @Override
    @Transactional
    public TareaEntity findById(Long id) {
        return tareaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        tareaDao.deleteById(id);
    }
}