package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.NotificacionEntity;
import com.usta.startupconnect.entities.UsuarioEntity;

import java.util.List;

public interface NotificacionService {
    void notificarEnSegundoPlano(String mensaje, String tipoEntidad, Long entidadId, UsuarioEntity usuario);

    void notificarNuevaConvocatoriaATodosLosEmprendedores(String mensaje);
    
    List<NotificacionEntity> obtenerNotificacionesPorUsuario(String documentoUsuario);
    
    // Nuevos métodos para marcar notificaciones como leídas
    void marcarNotificacionComoLeida(Long notificacionId);
    
    void marcarTodasNotificacionesComoLeidas(String documentoUsuario);
    
    NotificacionEntity obtenerNotificacionPorId(Long notificacionId);
    
    void notificarUsuariosPorRol(String rol, String mensaje);

    void notificarUsuario(String documentoUsuario, String mensaje);
}