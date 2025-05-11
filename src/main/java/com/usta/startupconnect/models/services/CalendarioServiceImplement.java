package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.CalendarioEntity;
import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.models.dao.CalendarioDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarioServiceImplement implements CalendarioService {
    @Autowired
    private CalendarioDao calendarioDao;

    @Override
    @Transactional(readOnly = true)
    public List<CalendarioEntity> findAll() {
        return (List<CalendarioEntity>) calendarioDao.findAll();
    }
}