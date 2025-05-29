package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.NotificacionEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.dao.NotificacionDao;
import com.usta.startupconnect.models.dao.UsuarioDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionServiceImplement implements NotificacionService {

    @Autowired
    private NotificacionDao notificacionDao;

    @Autowired
    private UsuarioDao usuarioDao;

    @Override
    public NotificacionEntity crearNotificacion(String mensaje, String tipoEntidad, Long entidadId, String idUsuario) {
        Optional<UsuarioEntity> usuarioOpt = usuarioDao.findById(idUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + idUsuario);
        }

        NotificacionEntity notificacion = new NotificacionEntity();
        notificacion.setMensaje(mensaje);
        notificacion.setTipoEntidad(tipoEntidad);
        notificacion.setEntidadId(entidadId);
        notificacion.setFecha(new Date());
        notificacion.setLeido(false);
        notificacion.setUsuario(usuarioOpt.get());

        return notificacionDao.save(notificacion);
    }

    @Override
    public List<NotificacionEntity> obtenerNotificacionesPorUsuario(String idUsuario) {
        return notificacionDao.findByUsuarioIdOrderByFechaDesc(idUsuario);
    }

    @Override
    public void marcarComoLeida(Long idNotificacion) {
        Optional<NotificacionEntity> notificacionOpt = notificacionDao.findById(idNotificacion);
        notificacionOpt.ifPresent(notificacion -> {
            notificacion.setLeido(true);
            notificacionDao.save(notificacion);
        });
    }
}
