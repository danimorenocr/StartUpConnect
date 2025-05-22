package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.entities.RolEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface UsuarioDao extends CrudRepository<UsuarioEntity, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT u FROM UsuarioEntity u WHERE u.rol = ?1")
    List<UsuarioEntity> findByRol(RolEntity rol);


    @Transactional(readOnly = true)
    @Query("SELECT u FROM UsuarioEntity u WHERE u.emailUsu = ?1")
    UsuarioEntity findByEmail(String email);

    @Transactional(readOnly = true)
    @Query("SELECT u FROM UsuarioEntity u WHERE u.nombreUsu LIKE %?1%")
    List<UsuarioEntity> findByNombreContaining(String nombre);

    @Transactional(readOnly = true)
    @Query("SELECT u FROM UsuarioEntity u WHERE u.fecha_creacion BETWEEN ?1 AND ?2")
    List<UsuarioEntity> findByFechaCreacionBetween(Date fechaInicio, Date fechaFin);

    @Transactional(readOnly = true)
    @Query("SELECT u FROM UsuarioEntity u WHERE u.telefono = ?1")
    UsuarioEntity findByTelefono(String telefono);

    @Transactional(readOnly = true)
    @Query("SELECT u FROM UsuarioEntity u WHERE u.emailUsu = ?1 AND u.contrasenna = ?2")
    UsuarioEntity findByEmailAndContrasenna(String email, String contrasenna);



}
