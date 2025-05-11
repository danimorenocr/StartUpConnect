package com.usta.startupconnect.models.services;

import java.util.Date;
import java.util.List;

import com.usta.startupconnect.entities.CalendarioEntity;
import com.usta.startupconnect.entities.NotificacionEntity;

public interface NotificacionService {
    public List<NotificacionEntity> findAll();

    
}
