package com.usta.startupconnect.controllers;

import com.google.api.services.calendar.model.Event;
import com.usta.startupconnect.dto.MeetingResponse;
import com.usta.startupconnect.exception.MeetingCreationException;
import com.usta.startupconnect.models.services.GoogleMeetService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/meet")
public class GoogleMeetController {
    
    private static final Logger logger = LoggerFactory.getLogger(GoogleMeetController.class);

    @Autowired
    private GoogleMeetService googleMeetService;    @GetMapping("/test")
    public String testMeetIntegration() {
        return "La integración de Google Meet está configurada correctamente. Usa el endpoint /api/meet/create para crear reuniones.";
    }
    
    @GetMapping("/test-create")
    public ResponseEntity<?> testCreateMeet() {
        try {
            // Crear una reunión para un día después de la fecha actual
            String summary = "Reunión de prueba automática";
            String description = "Esta es una reunión de prueba creada automáticamente";
            
            // Fecha actual más un día, a las 10 de la mañana
            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0);
            String start = tomorrow.format(DateTimeFormatter.ISO_DATE_TIME);
            
            // La reunión dura una hora
            String end = tomorrow.plusHours(1).format(DateTimeFormatter.ISO_DATE_TIME);
            
            logger.info("Creando reunión de prueba para: {}", start);
            
            Event event = googleMeetService.createMeetEvent(summary, description, start, end);
            
            if (event == null || event.getConferenceData() == null || 
                event.getConferenceData().getEntryPoints() == null || 
                event.getConferenceData().getEntryPoints().isEmpty()) {
                
                throw new MeetingCreationException("No se pudo obtener el enlace de la reunión");
            }
            
            String meetLink = event.getConferenceData().getEntryPoints().get(0).getUri();
            String eventId = event.getId();
            
            MeetingResponse response = new MeetingResponse(
                eventId,
                meetLink,
                "Reunión de prueba creada exitosamente",
                summary,
                start,
                end
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al crear la reunión de prueba", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMeet(
            @RequestParam String summary,
            @RequestParam String description,
            @RequestParam String start,
            @RequestParam String end) {
        
        try {
            logger.info("Creating meeting with summary: {}, start: {}, end: {}", summary, start, end);
            
            Event event = googleMeetService.createMeetEvent(summary, description, start, end);
            
            if (event == null || event.getConferenceData() == null || 
                event.getConferenceData().getEntryPoints() == null || 
                event.getConferenceData().getEntryPoints().isEmpty()) {
                
                throw new MeetingCreationException("No se pudo obtener el enlace de la reunión");
            }
            
            String meetLink = event.getConferenceData().getEntryPoints().get(0).getUri();
            String eventId = event.getId();
            
            MeetingResponse response = new MeetingResponse(
                eventId,
                meetLink,
                "Reunión creada exitosamente",
                summary,
                start,
                end
            );
            
            return ResponseEntity.ok(response);
            
        } catch (MeetingCreationException e) {
            logger.error("Error específico al crear la reunión: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MeetingResponse(null, null, "Error: " + e.getMessage(), summary, start, end));
        } catch (Exception e) {
            logger.error("Error inesperado al crear la reunión", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MeetingResponse(null, null, "Error interno del servidor: " + e.getMessage(), 
                                             summary, start, end));
        }
    }
}