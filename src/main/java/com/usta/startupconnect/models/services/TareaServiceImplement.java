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
    private TareaDao tareaDAO;

    @Override
    @Transactional
    public List<TareaEntity> findAll() {
        return (List<TareaEntity>) tareaDAO.findAll();
    }
}