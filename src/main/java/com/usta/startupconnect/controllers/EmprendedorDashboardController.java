package com.usta.startupconnect.controllers;

import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.NotificacionEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.EmprendedorService;
import com.usta.startupconnect.models.services.GoogleMeetService;
import com.usta.startupconnect.models.services.NotificacionService;
import com.usta.startupconnect.models.services.StartupService;
import com.usta.startupconnect.models.services.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class EmprendedorDashboardController {
    private static final Logger logger = LoggerFactory.getLogger(EmprendedorDashboardController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmprendedorService emprendedorService;

    @Autowired
    private StartupService startupService;

    @Autowired
    private GoogleMeetService googleMeetService;

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping(value = "/emprendedor/dashboardEmprendedor")
    public String dashboardEmprendedor(Model model, Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getName();
            UsuarioEntity usuario = usuarioService.findByEmail(email);
            if (usuario != null) {
                model.addAttribute("nombreCompleto", usuario.getNombreUsu());
                model.addAttribute("userEmail", email);
                model.addAttribute("documentoUsuario", usuario.getDocumento());
                System.out.println("Documento de usuario: " + usuario.getDocumento());

                // Obtener datos del emprendedor
                EmprendedorEntity emprendedor = emprendedorService.findByUsuarioEmail(email);
                if (emprendedor != null) {
                    model.addAttribute("emprendedor", emprendedor);
                    System.out.println("Emprendedor encontrado con documento: " + emprendedor.getDocumento());

                    // Obtener startups del emprendedor
                    List<StartupEntity> startups = startupService.findByEmprendedor(emprendedor);
                    model.addAttribute("startups", startups);
                    model.addAttribute("totalStartups", startups.size());
                }
                
                // Obtener notificaciones del usuario
                List<NotificacionEntity> notificaciones = notificacionService.obtenerNotificacionesPorUsuario(usuario.getDocumento());
                model.addAttribute("notificaciones", notificaciones);
                model.addAttribute("totalNotificaciones", notificaciones.size());
                logger.info("Cargadas {} notificaciones para el usuario {}", notificaciones.size(), usuario.getDocumento());
            } else {
                model.addAttribute("nombreCompleto", "Emprendedor");
            }
        } else {
            model.addAttribute("nombreCompleto", "Emprendedor");
        }

        // Obtener eventos próximos y configuración del calendario
        try {
            // Obtener eventos próximos
            List<Event> upcomingEvents = googleMeetService.listUpcomingEvents(5);
            model.addAttribute("upcomingEvents", upcomingEvents);

            // Obtener URL del calendario con autenticación
            String calendarEmbedUrl = googleMeetService.getCalendarEmbedUrl();
            model.addAttribute("calendarEmbedUrl", calendarEmbedUrl);

            // Obtener lista de calendarios disponibles (opcional, para futuras mejoras)
            List<CalendarListEntry> calendars = googleMeetService.getCalendars();
            model.addAttribute("calendars", calendars);
        } catch (Exception e) {
            logger.error("Error al cargar eventos de Google Calendar: {}", e.getMessage(), e);
            model.addAttribute("calendarError", "No se pudieron cargar los eventos: " + e.getMessage());
            // Agregar valores por defecto para que la plantilla no falle
            model.addAttribute("upcomingEvents", Collections.emptyList());
            model.addAttribute("calendarEmbedUrl", "about:blank");
            model.addAttribute("calendars", Collections.emptyList());
        }

        return "emprendedor/dashboardEmprendedor";
    }
}
