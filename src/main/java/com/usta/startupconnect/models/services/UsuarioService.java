package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.UsuarioEntity;

import java.util.List;

public interface UsuarioService {
    List<UsuarioEntity> findAll();

    public void save(UsuarioEntity usuario);

    public UsuarioEntity findById(String id);

    public void deleteById(String id);

    UsuarioEntity findByEmail(String email);
}
