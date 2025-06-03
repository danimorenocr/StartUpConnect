package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.NotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionDao extends JpaRepository<NotificacionEntity, Long> {
    // Método para obtener notificaciones de un usuario ordenadas por fecha
    List<NotificacionEntity> findByUsuario_DocumentoOrderByFechaDesc(String documento);
    
    // Método para marcar una notificación como leída
    @Modifying
    @Query("UPDATE NotificacionEntity n SET n.leido = true WHERE n.id = :id")
    void marcarComoLeida(@Param("id") Long id);
    
    // Método para marcar todas las notificaciones de un usuario como leídas
    @Modifying
    @Query("UPDATE NotificacionEntity n SET n.leido = true WHERE n.usuario.documento = :documento AND n.leido = false")
    void marcarTodasComoLeidas(@Param("documento") String documento);
}