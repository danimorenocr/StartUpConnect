package com.usta.startupconnect.models.services;

import com.usta.startupconnect.controllers.NotificacionController;
import com.usta.startupconnect.entities.NotificacionEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.dao.NotificacionDao;
import com.usta.startupconnect.models.dao.UsuarioDao;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionDao notificacionDAO;
    private final NotificacionController notificacionController;
    private final UsuarioDao usuarioDao;

    @Override
    @Async
    public void notificarEnSegundoPlano(String mensaje, String tipoEntidad, Long entidadId, UsuarioEntity usuario) {
        try {
            // Simula una tarea larga
            Thread.sleep(3000);

            NotificacionEntity noti = new NotificacionEntity();
            noti.setMensaje(mensaje);
            noti.setTipoEntidad(tipoEntidad);
            noti.setEntidadId(entidadId);
            noti.setFecha(new Date());
            noti.setLeido(false);
            noti.setUsuario(usuario);

            // Guardar en BD
            notificacionDAO.save(noti);

            // Enviar por WebSocket
            notificacionController.enviarNotificacionAUsuario(noti);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void notificarNuevaConvocatoriaATodosLosEmprendedores(String mensaje) {
        List<UsuarioEntity> emprendedores = usuarioDao.findByRol("EMPRENDEDOR");

        for (UsuarioEntity usuario : emprendedores) {
            NotificacionEntity noti = new NotificacionEntity();
            noti.setMensaje(mensaje);
            noti.setTipoEntidad("Convocatoria");
            noti.setEntidadId(null); // si quieres puedes asociar el ID de la convocatoria
            noti.setFecha(new Date());
            noti.setLeido(false);
            noti.setUsuario(usuario);

            notificacionDAO.save(noti);
            notificacionController.enviarNotificacionAUsuario(noti);
        }
    }
}