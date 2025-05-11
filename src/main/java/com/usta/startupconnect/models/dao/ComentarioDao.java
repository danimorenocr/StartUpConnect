package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.ComentariosEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ComentarioDao extends CrudRepository<ComentariosEntity, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT c FROM ComentariosEntity c WHERE c.usuario.documento = ?1")
    List<ComentariosEntity> findByUsuarioId(Long idUsuario);

    @Transactional(readOnly = true)
    @Query("SELECT c FROM ComentariosEntity c WHERE c.startup.id = ?1")
    List<ComentariosEntity> findByStartupId(Long idStartup);

    @Transactional(readOnly = true)
    @Query("SELECT c FROM ComentariosEntity c WHERE c.startup = ?1")
    List<ComentariosEntity> findByStartup(StartupEntity startup);

    @Transactional(readOnly = true)
    @Query("SELECT c FROM ComentariosEntity c WHERE c.usuario = ?1")
    List<ComentariosEntity> findByUsuario(UsuarioEntity usuario);

    @Transactional
    @Modifying
    @Query("DELETE FROM ComentariosEntity c WHERE c.idComentario = ?1 AND c.usuario.documento = ?2")
    int eliminarComentario(Long idComentario, Long idUsuario);
}
