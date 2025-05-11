package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.entities.RolEntity;

import java.util.Date;
import java.util.List;

public interface UsuarioService {
    List<UsuarioEntity> findAll();

}
