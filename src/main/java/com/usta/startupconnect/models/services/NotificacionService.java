package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.NotificacionEntity;

import java.util.List;

public interface NotificacionService {

    NotificacionEntity crearNotificacion(String mensaje, String tipoEntidad, Long entidadId, String idUsuario);

    List<NotificacionEntity> obtenerNotificacionesPorUsuario(String idUsuario);

    void marcarComoLeida(Long idNotificacion);
}
