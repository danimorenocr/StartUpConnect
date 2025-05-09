package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.NotificacionEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface NotificacionDao extends CrudRepository<NotificacionEntity, Long> {

    @Transactional
    @Query("SELECT n FROM NotificacionEntity n WHERE n.usuario.idUsu = ?1 ORDER BY n.fechaNotificacion DESC")
    public List<NotificacionEntity> findByUsuarioId(Long idUsuario);
    
    @Transactional
    @Query("SELECT n FROM NotificacionEntity n WHERE n.leida = false AND n.usuario.idUsu = ?1")
    public List<NotificacionEntity> findNotificacionesNoLeidas(Long idUsuario);
    
    @Transactional
    @Query("SELECT n FROM NotificacionEntity n WHERE n.fechaNotificacion BETWEEN ?1 AND ?2 AND n.usuario.idUsu = ?3")
    public List<NotificacionEntity> findByFechaAndUsuario(Date fechaInicio, Date fechaFin, Long idUsuario);
    
    @Transactional
    @Modifying
    @Query("UPDATE NotificacionEntity n SET n.leida = true WHERE n.idNotificacion = ?1")
    public int marcarComoLeida(Long idNotificacion);
    
    @Transactional
    @Modifying
    @Query("UPDATE NotificacionEntity n SET n.leida = true WHERE n.usuario.idUsu = ?1")
    public int marcarTodasComoLeidas(Long idUsuario);
}
