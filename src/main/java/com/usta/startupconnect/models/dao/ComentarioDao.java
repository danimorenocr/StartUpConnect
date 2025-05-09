package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.ComentariosEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ComentarioDao extends CrudRepository<ComentariosEntity, Long> {

    @Transactional
    @Query("SELECT c FROM ComentariosEntity c WHERE c.usuario.idUsu = ?1")
    public List<ComentariosEntity> findByUsuarioId(Long idUsuario);
    
    @Transactional
    @Query("SELECT c FROM ComentariosEntity c WHERE c.entregable.idEntregable = ?1")
    public List<ComentariosEntity> findByEntregableId(Long idEntregable);
    
    @Transactional
    @Query("SELECT c FROM ComentariosEntity c WHERE c.fechaComentario BETWEEN ?1 AND ?2")
    public List<ComentariosEntity> findByFechaBetween(Date fechaInicio, Date fechaFin);
    
    @Transactional
    @Modifying
    @Query("UPDATE ComentariosEntity c SET c.contenidoComentario = ?2 WHERE c.idComentario = ?1")
    public int actualizarContenidoComentario(Long idComentario, String contenido);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM ComentariosEntity c WHERE c.idComentario = ?1 AND c.usuario.idUsu = ?2")
    public int eliminarComentario(Long idComentario, Long idUsuario);
}
