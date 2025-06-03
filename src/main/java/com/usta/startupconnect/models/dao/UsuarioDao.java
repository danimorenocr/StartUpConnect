package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.entities.RolEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsuarioDao extends JpaRepository<UsuarioEntity, String> {

    @Transactional
    @Query("SELECT u FROM UsuarioEntity u WHERE u.rol = ?1")
    List<UsuarioEntity> findByRol(RolEntity rol);

    @Transactional
    @Query("SELECT u FROM UsuarioEntity u WHERE u.id = ?1")
    UsuarioEntity findUsuarioById(String id);

    @Transactional
    @Query("SELECT u FROM UsuarioEntity u WHERE u.emailUsu = ?1")
    UsuarioEntity findByEmail(String email);

    @Transactional
    @Query("SELECT u FROM UsuarioEntity u WHERE u.nombreUsu LIKE %?1%")
    List<UsuarioEntity> findByNombreContaining(String nombre);

    @Transactional
    @Query("SELECT u FROM UsuarioEntity u WHERE u.telefono = ?1")
    UsuarioEntity findByTelefono(String telefono);

    @Transactional
    @Query("SELECT u FROM UsuarioEntity u WHERE u.emailUsu = ?1 AND u.contrasenna = ?2")
    UsuarioEntity findByEmailAndContrasenna(String email, String contrasenna);

    List<UsuarioEntity> findByRol(String rol);

}
