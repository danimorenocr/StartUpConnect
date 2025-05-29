package com.usta.startupconnect.controllers;

import com.usta.startupconnect.dto.NotificacionDTO;
import com.usta.startupconnect.entities.NotificacionEntity;
import com.usta.startupconnect.models.services.NotificacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    // Crear notificación
    @PostMapping
    public ResponseEntity<NotificacionEntity> crearNotificacion(@RequestBody NotificacionDTO dto) {
        NotificacionEntity notificacion = notificacionService.crearNotificacion(
                dto.getMensaje(),
                dto.getTipoEntidad(),
                dto.getEntidadId(),
                dto.getIdUsuario());
        return ResponseEntity.ok(notificacion);
    }

    // Obtener notificaciones por usuario
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<NotificacionEntity>> obtenerPorUsuario(@PathVariable String idUsuario) {
        List<NotificacionEntity> notificaciones = notificacionService.obtenerNotificacionesPorUsuario(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }

    // Marcar como leída
    @PutMapping("/{id}/leido")
    public ResponseEntity<?> marcarComoLeida(@PathVariable Long id) {
        notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok("Notificación marcada como leída");
    }
}
