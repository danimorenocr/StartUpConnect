package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.dao.UsuarioDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImplement implements UsuarioService {
    @Autowired
    private UsuarioDao usuarioDao;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEntity> findAll() {
        return (List<UsuarioEntity>) usuarioDao.findAll();
    }

    @Override
    @jakarta.transaction.Transactional
    public void save(UsuarioEntity usuario) {
        usuarioDao.save(usuario);
    }

    @Override
    @Transactional
    public UsuarioEntity findById(String id) {
        return usuarioDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        usuarioDao.deleteById(id);
    }

}