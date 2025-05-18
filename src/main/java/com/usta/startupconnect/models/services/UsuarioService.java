package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.UsuarioEntity;

import java.util.List;

public interface UsuarioService {
    List<UsuarioEntity> findAll();

    public void save(UsuarioEntity usuario);

    public UsuarioEntity findById(Long id);

    public void deleteById(Long id);
}
