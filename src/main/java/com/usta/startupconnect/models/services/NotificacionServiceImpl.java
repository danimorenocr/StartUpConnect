package com.usta.startupconnect.models.services;


import com.usta.startupconnect.dto.NotificacionDTO;
import com.usta.startupconnect.entities.NotificacionEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.dao.NotificacionDao;
import com.usta.startupconnect.models.dao.UsuarioDao;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionDao notificacionDAO;
    private final UsuarioDao usuarioDao;
    private final SimpMessagingTemplate messagingTemplate;

    private void enviarNotificacionAUsuario(NotificacionEntity notificacion) {
        NotificacionDTO dto = new NotificacionDTO(
                notificacion.getMensaje(),
                notificacion.getTipoEntidad(),
                notificacion.getEntidadId(),
                notificacion.getFecha(),
                notificacion.getLeido(),
                notificacion.getUsuario().getDocumento()
        );

        messagingTemplate.convertAndSend("/topic/notificaciones/" + notificacion.getUsuario().getDocumento(), dto);
    }

    @Override
    @Async
    public void notificarEnSegundoPlano(String mensaje, String tipoEntidad, Long entidadId, UsuarioEntity usuario) {
        try {
            Thread.sleep(3000);

            NotificacionEntity noti = new NotificacionEntity();
            noti.setMensaje(mensaje);
            noti.setTipoEntidad(tipoEntidad);
            noti.setEntidadId(entidadId);
            noti.setFecha(new Date());
            noti.setLeido(false);
            noti.setUsuario(usuario);

            notificacionDAO.save(noti);
            enviarNotificacionAUsuario(noti);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }    @Override
    @Async
    public void notificarNuevaConvocatoriaATodosLosEmprendedores(String mensaje) {
        List<UsuarioEntity> emprendedores = usuarioDao.findByRol_Rol("EMPRENDEDOR");

        for (UsuarioEntity usuario : emprendedores) {
            NotificacionEntity noti = new NotificacionEntity();
            noti.setMensaje(mensaje);
            noti.setTipoEntidad("Convocatoria");
            noti.setEntidadId(null);
            noti.setFecha(new Date());
            noti.setLeido(false);
            noti.setUsuario(usuario);

            notificacionDAO.save(noti);
            enviarNotificacionAUsuario(noti);
        }
    }
    
    @Override
    public List<NotificacionEntity> obtenerNotificacionesPorUsuario(String documentoUsuario) {
        return notificacionDAO.findByUsuario_DocumentoOrderByFechaDesc(documentoUsuario);
    }
    
    @Override
    @Transactional
    public void marcarNotificacionComoLeida(Long notificacionId) {
        notificacionDAO.marcarComoLeida(notificacionId);
    }
    
    @Override
    @Transactional
    public void marcarTodasNotificacionesComoLeidas(String documentoUsuario) {
        notificacionDAO.marcarTodasComoLeidas(documentoUsuario);
    }
    
    @Override
    @Transactional(readOnly = true)
    public NotificacionEntity obtenerNotificacionPorId(Long notificacionId) {
        return notificacionDAO.findById(notificacionId).orElse(null);
    }
}
