package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.ComentariosEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;

public interface ComentarioService {

    public List<ComentariosEntity> findAll();

    public void save(ComentariosEntity comentario);

    public ComentariosEntity findById(Long id);

    public void deleteById(Long id);

    public ComentariosEntity actualizar(ComentariosEntity comentario);

    public List<ComentariosEntity> findByStartup(StartupEntity startup);

    public List<ComentariosEntity> findByUsuario(UsuarioEntity usuario);
}
