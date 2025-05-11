package com.usta.startupconnect.models.services;

import java.sql.Date;
import java.util.List;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;

public interface ConvocatoriaService {
    public List<ConvocatoriaEntity> findAll();

}
