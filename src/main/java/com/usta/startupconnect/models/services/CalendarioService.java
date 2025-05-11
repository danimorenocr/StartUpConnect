package com.usta.startupconnect.models.services;

import java.util.Date;
import java.util.List;

import com.usta.startupconnect.entities.CalendarioEntity;
import com.usta.startupconnect.entities.ConvocatoriaEntity;

public interface CalendarioService {
    public List<CalendarioEntity> findAll();

}
