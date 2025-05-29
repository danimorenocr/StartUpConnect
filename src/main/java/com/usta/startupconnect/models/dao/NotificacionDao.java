package com.usta.startupconnect.models.dao;


import com.usta.startupconnect.entities.NotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionDao extends JpaRepository<NotificacionEntity, Long> {

    List<NotificacionEntity> findByUsuarioIdOrderByFechaDesc(String idUsuario);

}
