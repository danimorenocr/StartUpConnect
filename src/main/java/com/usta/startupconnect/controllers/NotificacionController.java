package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.NotificacionEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.NotificacionService;
import com.usta.startupconnect.models.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    
    @Autowired
    private NotificacionService notificacionService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/marcar-leida/{id}")
    public ResponseEntity<?> marcarComoLeida(@PathVariable Long id, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Verificar que la notificación existe
            NotificacionEntity notificacion = notificacionService.obtenerNotificacionPorId(id);
            if (notificacion == null) {
                response.put("mensaje", "La notificación no existe");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            // Verificar que el usuario autenticado es el dueño de la notificación
            String email = authentication.getName();
            UsuarioEntity usuario = usuarioService.findByEmail(email);
            
            if (usuario == null || !usuario.getDocumento().equals(notificacion.getUsuario().getDocumento())) {
                response.put("mensaje", "No tienes permiso para realizar esta acción");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
            
            // Marcar como leída
            notificacionService.marcarNotificacionComoLeida(id);
            
            response.put("mensaje", "Notificación marcada como leída");
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            response.put("mensaje", "Error al marcar la notificación como leída");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/marcar-todas-leidas")
    public ResponseEntity<?> marcarTodasComoLeidas(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String email = authentication.getName();
            UsuarioEntity usuario = usuarioService.findByEmail(email);
            
            if (usuario == null) {
                response.put("mensaje", "Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            notificacionService.marcarTodasNotificacionesComoLeidas(usuario.getDocumento());
            
            response.put("mensaje", "Todas las notificaciones han sido marcadas como leídas");
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            response.put("mensaje", "Error al marcar las notificaciones como leídas");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/admin")
    public ResponseEntity<?> obtenerNotificacionesAdmin(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = authentication.getName();
            UsuarioEntity usuario = usuarioService.findByEmail(email);

            if (usuario == null || !usuario.getRol().getRol().equals("ADMIN")) {
                response.put("mensaje", "No tienes permiso para realizar esta acción");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }

            List<Map<String, Object>> notificaciones = notificacionService.obtenerNotificacionesPorUsuario(usuario.getDocumento())
                .stream()
                .map(notificacion -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("id", notificacion.getId());
                    dto.put("titulo", notificacion.getTipoEntidad()); // Usar tipoEntidad como título
                    dto.put("mensaje", notificacion.getMensaje());
                    return dto;
                })
                .collect(Collectors.toList());

            return new ResponseEntity<>(notificaciones, HttpStatus.OK);

        } catch (Exception e) {
            response.put("mensaje", "Error al obtener las notificaciones");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/mentor")
    public ResponseEntity<?> obtenerNotificacionesMentor(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = authentication.getName();
            UsuarioEntity usuario = usuarioService.findByEmail(email);

            if (usuario == null || !usuario.getRol().getRol().equals("MENTOR")) {
                response.put("mensaje", "No tienes permiso para realizar esta acción");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }

            List<Map<String, Object>> notificaciones = notificacionService.obtenerNotificacionesPorUsuario(usuario.getDocumento())
                .stream()
                .map(notificacion -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("id", notificacion.getId());
                    dto.put("titulo", notificacion.getTipoEntidad()); // Usar tipoEntidad como título
                    dto.put("mensaje", notificacion.getMensaje());
                    dto.put("fecha", notificacion.getFecha());
                    return dto;
                })
                .collect(Collectors.toList());

            return new ResponseEntity<>(notificaciones, HttpStatus.OK);

        } catch (Exception e) {
            response.put("mensaje", "Error al obtener las notificaciones");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
