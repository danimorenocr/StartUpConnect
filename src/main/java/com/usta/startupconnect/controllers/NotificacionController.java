package com.usta.startupconnect.controllers;

import com.usta.startupconnect.dto.NotificacionDTO;
import com.usta.startupconnect.entities.NotificacionEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NotificacionController {

    private final SimpMessagingTemplate messagingTemplate;

    public void enviarNotificacionAUsuario(NotificacionEntity notificacion) {
        NotificacionDTO dto = new NotificacionDTO(
                notificacion.getMensaje(),
                notificacion.getTipoEntidad(),
                notificacion.getEntidadId(),
                notificacion.getFecha(),
                notificacion.getLeido(),
                notificacion.getUsuario().getDocumento() // <--- este es el campo faltante
        );

        // Enviar al usuario por su ID (por ejemplo, documento del usuario)
        messagingTemplate.convertAndSend("/topic/notificaciones/" + notificacion.getUsuario().getDocumento(), dto);
    }
}
