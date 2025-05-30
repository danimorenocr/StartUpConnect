package com.usta.startupconnect.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.EtapaService;
import com.usta.startupconnect.models.services.GoogleMeetService;
import com.usta.startupconnect.models.services.MentorService;
import com.usta.startupconnect.models.services.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MentorDashboardController {
    private static final Logger logger = LoggerFactory.getLogger(MentorDashboardController.class);
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private MentorService mentorService;
    
    @Autowired
    private EtapaService etapaService;
    
    @Autowired
    private GoogleMeetService googleMeetService;

    @GetMapping(value = "/mentor/dashboardMentor")
    public String dashboardMentor(Model model, Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getName();
            UsuarioEntity usuario = usuarioService.findByEmail(email);
            
            if (usuario != null) {
                model.addAttribute("nombreCompleto", usuario.getNombreUsu());
                model.addAttribute("userEmail", email);
                
                // Obtener datos del mentor
                MentorEntity mentor = mentorService.findByUsuarioEmail(email);
                if (mentor != null) {
                    model.addAttribute("mentor", mentor);
                      // Obtener etapas asignadas al mentor
                    List<EtapaEntity> etapas = etapaService.findByMentor(mentor);
                    model.addAttribute("totalEtapas", etapas.size());
                    
                    // Creamos una clasificación simple basada en fechas en lugar de estado
                    java.util.Date fechaActual = new java.util.Date();
                      Map<String, Long> etapasPorEstado = etapas.stream()
                            .collect(Collectors.groupingBy(
                                    etapa -> {
                                        if (etapa.getFechaInicio().after(fechaActual)) {
                                            return "Por iniciar";
                                        } else if (etapa.getFechaFin().before(fechaActual)) {
                                            return "Completada";
                                        } else {
                                            return "Activa";
                                        }
                                    },
                                    Collectors.counting()));
                    model.addAttribute("etapasPorEstado", etapasPorEstado);
                    
                    // Calcular el número de etapas activas (en progreso)
                    long etapasActivas = etapas.stream()
                            .filter(etapa -> etapa.getFechaInicio().before(fechaActual) && etapa.getFechaFin().after(fechaActual))
                            .count();
                    model.addAttribute("etapasActivas", etapasActivas);
                    
                    // Encontrar la próxima etapa a comenzar
                    EtapaEntity proximaEtapa = etapas.stream()
                            .filter(etapa -> etapa.getFechaInicio().after(fechaActual))
                            .min(Comparator.comparing(EtapaEntity::getFechaInicio))
                            .orElse(null);
                    model.addAttribute("proximaEtapa", proximaEtapa);
                }
            } else {
                model.addAttribute("nombreCompleto", "Mentor");
            }
        } else {
            model.addAttribute("nombreCompleto", "Mentor");
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
        
        return "mentor/dashboardMentor";
    }
}
