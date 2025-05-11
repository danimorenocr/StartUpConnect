package com.usta.startupconnect.models.services;

import java.util.List;
import java.util.Date;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.StartupEntity;

public interface StartupService {
    
    public List<StartupEntity> findAll();

}
