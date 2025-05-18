package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.NotificacionEntity;
import com.usta.startupconnect.models.dao.NotificacionDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacionServiceImplement implements NotificacionService {
    @Autowired
    private NotificacionDao notificacionDAO;

    @Override
    @Transactional
    public List<NotificacionEntity> findAll() {
        return (List<NotificacionEntity>) notificacionDAO.findAll();
    }
}