package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.ComentariosEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ComentariosDao extends CrudRepository<ComentariosEntity, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT c FROM ComentariosEntity c WHERE c.startup.id = ?1")
    List<ComentariosEntity> findByStartupId(Long id);


    @Transactional(readOnly = true)
    @Query("SELECT c FROM ComentariosEntity c WHERE c.usuario = ?1")
    List<ComentariosEntity> findByUsuario(UsuarioEntity usuario);

    @Transactional(readOnly = true)
    @Query("SELECT c FROM ComentariosEntity c WHERE c.comentario LIKE %?1%")
    List<ComentariosEntity> findByComentarioContaining(String texto);

    @Transactional(readOnly = true)
    @Query("SELECT COUNT(c) FROM ComentariosEntity c WHERE c.startup = ?1")
    Long countByStartup(StartupEntity startup);

    @Transactional(readOnly = true)
    @Query("SELECT COUNT(c) FROM ComentariosEntity c WHERE c.usuario = ?1")
    Long countByUsuario(UsuarioEntity usuario);
} 