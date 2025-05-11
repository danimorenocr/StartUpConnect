package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.ComentariosEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;

import java.util.List;

public interface ComentariosService {
    List<ComentariosEntity> findAll();

} 