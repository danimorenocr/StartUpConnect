package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.UsuarioEntity;

public interface UsuarioService {

    public List<UsuarioEntity> findAll();

    public void save(UsuarioEntity usuario);

    public UsuarioEntity findById(Long cedula);

    public void deletebyId(Long cedula);

    public UsuarioEntity actualizar(UsuarioEntity usuario);

    public UsuarioEntity findByEmail(String email);
}
