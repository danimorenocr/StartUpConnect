package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsuarioDao extends CrudRepository<UsuarioEntity, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT US FROM UsuarioEntity US WHERE US.emailUsu = ?1")
    public UsuarioEntity findByemail(String email);

    @Transactional(readOnly = true)
    @Query("SELECT u FROM UsuarioEntity u")
    public List<UsuarioEntity> findAllUsuarios();

    @Transactional(readOnly = true)
    @Query("SELECT u FROM UsuarioEntity u WHERE u.nombreUsu LIKE %?1%")
    public List<UsuarioEntity> findByNombreContaining(String nombre);
    
    @Transactional
    @Modifying
    @Query("UPDATE UsuarioEntity u SET u.estadoUsu = ?2 WHERE u.idUsu = ?1")
    public int updateEstadoUsuario(Long idUsuario, Boolean estado);
    
    @Transactional
    @Modifying
    @Query("UPDATE UsuarioEntity u SET u.nombreUsu = ?2, u.apellidoUsu = ?3, u.telefonoUsu = ?4 WHERE u.idUsu = ?1")
    public int updateDatosUsuario(Long idUsuario, String nombre, String apellido, String telefono);
}
