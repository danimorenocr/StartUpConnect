package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.models.dao.ConvocatoriaDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConvocatoriaServiceImplement implements ConvocatoriaService {
    @Autowired
    private ConvocatoriaDao convocatoriaDAO;

    @Override
    @Transactional(readOnly = true)
    public List<ConvocatoriaEntity> findAll() {
        return (List<ConvocatoriaEntity>) convocatoriaDAO.findAll();
    }
}