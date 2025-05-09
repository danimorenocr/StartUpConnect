package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.ComentariosEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.dao.ComentarioDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
public class ComentarioServiceImplement implements ComentarioService {
    @Autowired
    private ComentarioDao comentarioDAO;

    @Override
    @Transactional
    public List<ComentariosEntity> findAll() {
        return (List<ComentariosEntity>) comentarioDAO.findAll();
    }

    @Override
    @Transactional
    public void save(ComentariosEntity comentario) {
        comentarioDAO.save(comentario);
    }

    @Override
    @Transactional
    public ComentariosEntity findById(Long id) {
        return comentarioDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        comentarioDAO.deleteById(id);
    }

    @Override
    @Transactional
    public ComentariosEntity actualizar(ComentariosEntity comentario) {
        return comentarioDAO.save(comentario);
    }

    @Override
    @Transactional
    public List<ComentariosEntity> findByStartup(StartupEntity startup) {
        // El método findByStartup no existe en el DAO
        // Implementación temporal: devolver lista vacía o null
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public List<ComentariosEntity> findByUsuario(UsuarioEntity usuario) {
        // Convertir el documento (String) a Long para que coincida con el tipo esperado
        try {
            Long usuarioId = Long.valueOf(usuario.getDocumento());
            return comentarioDAO.findByUsuarioId(usuarioId);
        } catch (NumberFormatException e) {
            // Si el documento no es un número válido, devolver lista vacía
            return new ArrayList<>();
        }
    }
}