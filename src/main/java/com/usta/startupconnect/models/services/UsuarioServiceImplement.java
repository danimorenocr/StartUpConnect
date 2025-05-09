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
    private UsuarioDao usuarioDAO;

    @Override
    @Transactional
    public List<UsuarioEntity> findAll() {
        return (List<UsuarioEntity>) usuarioDAO.findAll();
    }

    @Override
    @Transactional
    public void save(UsuarioEntity usuario) {
        usuarioDAO.save(usuario);
    }

    @Override
    @Transactional
    public UsuarioEntity findById(Long cedula) {
        return usuarioDAO.findById(cedula).orElse(null);
    }

    @Override
    @Transactional
    public void deletebyId(Long cedula) {
        usuarioDAO.deleteById(cedula);
    }

    @Override
    @Transactional
    public UsuarioEntity actualizar(UsuarioEntity usuario) {
        return usuarioDAO.save(usuario);
    }
    
    @Override
    @Transactional
    public UsuarioEntity findByEmail(String email) {
        return usuarioDAO.findByemail(email);
    }
}