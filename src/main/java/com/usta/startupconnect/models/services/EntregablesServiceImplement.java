package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.EntregableEntity;
import com.usta.startupconnect.entities.TareaEntity;
import com.usta.startupconnect.models.dao.EntregablesDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntregablesServiceImplement implements EntregablesService {
    @Autowired
    private EntregablesDao entregablesDAO;

    @Override
    @Transactional
    public List<EntregableEntity> findAll() {
        return (List<EntregableEntity>) entregablesDAO.findAll();
    }
}