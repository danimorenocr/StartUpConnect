package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.NotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionDao extends JpaRepository<NotificacionEntity, Long> {
    // Puedes agregar métodos personalizados si necesitas
}