package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.TareaEntity;
import com.usta.startupconnect.entities.EntregableEntity;
import com.usta.startupconnect.models.services.EtapaService;
import com.usta.startupconnect.models.services.GoogleMeetService;
import com.usta.startupconnect.models.services.TareaService;
import com.usta.startupconnect.models.services.EntregablesService;
import com.google.api.services.calendar.model.Event;
import com.usta.startupconnect.dto.MeetingResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

@Controller
public class TareaController {
    @Autowired
    private TareaService tareaService;
    @Autowired
    private EtapaService etapaService;
    @Autowired
    private GoogleMeetService googleMeetService;
    @Autowired
    private com.usta.startupconnect.models.services.EntregablesService entregablesService;

    @GetMapping(value = "/tarea")
    public String listaTareas(Model model) {
        model.addAttribute("title", "Tareas");
        model.addAttribute("urlRegistro", "/crearTarea");
        List<TareaEntity> lista = tareaService.findAll();
        lista.sort(Comparator.comparing(TareaEntity::getId));
        model.addAttribute("tareas", lista);
        return "/tarea/listarTareas";
    }    @GetMapping(value = "/crearTarea")
    public String crearTarea(Model model) {
        List<EtapaEntity> etapas = etapaService.findAll();
        model.addAttribute("etapas", etapas);
        model.addAttribute("title", "Crear Tarea");
        TareaEntity tarea = new TareaEntity();
        model.addAttribute("tarea", tarea);
        // Obtener miembros del equipo para reunión (implementar según tu modelo de datos)
        // Ejemplo: List<MiembroEntity> miembros = miembroService.findAll();
        // model.addAttribute("miembrosEquipo", miembros);
        return "/tarea/crearTarea";
    }

    @PostMapping("/crearTarea")
    public String crearTarea(
            @Valid @ModelAttribute("tarea") TareaEntity tarea,
            BindingResult result,
            @RequestParam(required = false) Boolean crearReunion,
            @RequestParam(required = false) String fechaReunion,
            @RequestParam(required = false) String horaReunion,
            @RequestParam(required = false) Integer duracionReunion,
            @RequestParam(required = false) List<Long> participantesReunion,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            List<EtapaEntity> etapas = etapaService.findAll();
            model.addAttribute("etapas", etapas);
            // Volver a cargar lista de miembros si es necesario
            // model.addAttribute("miembrosEquipo", miembroService.findAll());
            return "tarea/crearTarea";
        }

        // Guardar la tarea
        tareaService.save(tarea);
        
        // Si se solicitó crear una reunión
        if (crearReunion != null && crearReunion) {
            try {
                // Combinar fecha y hora
                LocalDate fecha = LocalDate.parse(fechaReunion);
                LocalTime hora = LocalTime.parse(horaReunion);
                LocalDateTime fechaHoraInicio = LocalDateTime.of(fecha, hora);
                
                // Calcular fecha/hora fin según duración
                LocalDateTime fechaHoraFin = fechaHoraInicio.plusMinutes(duracionReunion);
                
                // Formatear para la API de Google Calendar
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                String start = fechaHoraInicio.format(formatter);
                String end = fechaHoraFin.format(formatter);
                
                // Título y descripción para la reunión
                String summary = "Reunión: " + tarea.getTitulo();
                String description = "Reunión para la tarea: " + tarea.getTitulo();
                if (tarea.getDescripcion() != null && !tarea.getDescripcion().isEmpty()) {
                    description += "\n\nDetalles de la tarea: " + tarea.getDescripcion();
                }
                
                // Crear la reunión usando el servicio
                Event event = googleMeetService.createMeetEvent(summary, description, start, end);
                  if (event != null && event.getConferenceData() != null && 
                    event.getConferenceData().getEntryPoints() != null && 
                    !event.getConferenceData().getEntryPoints().isEmpty()) {
                    
                    // Obtener el enlace de la reunión
                    String meetLink = event.getConferenceData().getEntryPoints().get(0).getUri();
                    
                    // Añadir el enlace de la reunión a la descripción de la tarea
                    String descripcionActual = tarea.getDescripcion();
                    String nuevaDescripcion = descripcionActual + "\n\n----- REUNIÓN DE GOOGLE MEET -----\n" +
                                              "Enlace de la reunión: " + meetLink + 
                                              "\nFecha: " + fechaReunion + " a las " + horaReunion + 
                                              "\nDuración: " + duracionReunion + " minutos";
                    tarea.setDescripcion(nuevaDescripcion);
                    
                    // Guardar la tarea actualizada
                    tareaService.save(tarea);
                    
                    // Aquí puedes manejar los participantes si tienes una entidad para esto
                    // if (participantesReunion != null && !participantesReunion.isEmpty()) {
                    //     for (Long participanteId : participantesReunion) {
                    //         // Agregar participante a la reunión
                    //     }
                    // }
                    
                    redirectAttributes.addFlashAttribute("mensajeExito", 
                        "Tarea creada exitosamente con reunión de Google Meet programada.");
                } else {
                    redirectAttributes.addFlashAttribute("mensajeAdvertencia", 
                        "Tarea creada pero hubo un problema al generar el enlace de la reunión.");
                }
                
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("mensajeError", 
                    "Tarea creada pero ocurrió un error al crear la reunión: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("mensajeExito", "Tarea creada exitosamente.");
        }
        
        return "redirect:/tarea";
    }

    @GetMapping(value = "/editarTarea/{id}")
    public String editarTarea(Model model, @PathVariable(value = "id") Long id) {
        TareaEntity tarea = tareaService.findById(id);
        List<EtapaEntity> etapas = etapaService.findAll();
        model.addAttribute("etapas", etapas);
        model.addAttribute("title", "Editar tarea");
        model.addAttribute("tareaEditar", tarea);
        
        // Extraer la información de la reunión si existe
        if (tarea.getDescripcion() != null && tarea.getDescripcion().contains("REUNIÓN DE GOOGLE MEET")) {
            com.usta.startupconnect.dto.MeetingInfo meetingInfo = 
                googleMeetService.extractMeetingInfoFromDescription(tarea.getDescripcion());
            model.addAttribute("meetingInfo", meetingInfo);
        }
        
        return "tarea/editarTarea";
    }    @PostMapping("/editarTarea/{id}")
    public String editarEvento(@Valid TareaEntity tarea,
            BindingResult result,
            @RequestParam(required = false) Boolean editarReunion,
            @RequestParam(required = false) String eventId,
            @RequestParam(required = false) String fechaReunion,
            @RequestParam(required = false) String horaReunion,
            @RequestParam(required = false) Integer duracionReunion,
            @RequestParam(required = false) String enlaceReunion,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            List<EtapaEntity> etapas = etapaService.findAll();
            model.addAttribute("etapas", etapas);
            return "tarea/editarTarea";
        }

        // Obtener la tarea existente para conservar algunos datos
        TareaEntity tareaExistente = tareaService.findById(tarea.getId());
        tarea.setFechaEntrega(tareaExistente.getFechaEntrega());
        
        // Procesar la actualización de la reunión si se solicitó
        if (editarReunion != null && editarReunion) {
            try {
                String descripcionActual = tareaExistente.getDescripcion();
                boolean tieneReunionExistente = descripcionActual != null && descripcionActual.contains("REUNIÓN DE GOOGLE MEET");
                
                // Combinar fecha y hora para la reunión
                LocalDate fecha = LocalDate.parse(fechaReunion);
                LocalTime hora = LocalTime.parse(horaReunion);
                LocalDateTime fechaHoraInicio = LocalDateTime.of(fecha, hora);
                
                // Calcular fecha/hora fin según duración
                LocalDateTime fechaHoraFin = fechaHoraInicio.plusMinutes(duracionReunion);
                
                // Formatear para la API de Google Calendar
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                String start = fechaHoraInicio.format(formatter);
                String end = fechaHoraFin.format(formatter);
                
                // Título y descripción para la reunión
                String summary = "Reunión: " + tarea.getTitulo();
                String meetDescription = "Reunión para la tarea: " + tarea.getTitulo();
                if (tarea.getDescripcion() != null && !tarea.getDescripcion().isEmpty()) {
                    // Eliminar la parte de la reunión si ya existe
                    String descripcionLimpia = tarea.getDescripcion();
                    int indexReunion = descripcionLimpia.indexOf("----- REUNIÓN DE GOOGLE MEET -----");
                    if (indexReunion > 0) {
                        descripcionLimpia = descripcionLimpia.substring(0, indexReunion).trim();
                    }
                    meetDescription += "\n\nDetalles de la tarea: " + descripcionLimpia;
                }
                
                Event event = null;
                String meetLink = "";
                
                // Si ya existe una reunión, actualizarla
                if (tieneReunionExistente && eventId != null && !eventId.isEmpty()) {
                    event = googleMeetService.updateEvent(eventId, summary, meetDescription, start, end);
                    if (event != null && event.getConferenceData() != null && 
                        event.getConferenceData().getEntryPoints() != null && 
                        !event.getConferenceData().getEntryPoints().isEmpty()) {
                        
                        meetLink = event.getConferenceData().getEntryPoints().get(0).getUri();
                        redirectAttributes.addFlashAttribute("mensajeExito", 
                            "Tarea y reunión de Google Meet actualizadas exitosamente.");
                    }
                } 
                // Si no existe una reunión o no se pudo actualizar, crear una nueva
                else {
                    event = googleMeetService.createMeetEvent(summary, meetDescription, start, end);
                    if (event != null && event.getConferenceData() != null && 
                        event.getConferenceData().getEntryPoints() != null && 
                        !event.getConferenceData().getEntryPoints().isEmpty()) {
                        
                        meetLink = event.getConferenceData().getEntryPoints().get(0).getUri();
                        redirectAttributes.addFlashAttribute("mensajeExito", 
                            "Tarea actualizada y nueva reunión de Google Meet creada exitosamente.");
                    }
                }
                
                // Si se obtuvo el enlace de la reunión, actualizar la descripción de la tarea
                if (!meetLink.isEmpty()) {
                    // Eliminar la parte de la reunión si ya existe
                    String descripcionLimpia = tarea.getDescripcion();
                    int indexReunion = descripcionLimpia.indexOf("----- REUNIÓN DE GOOGLE MEET -----");
                    if (indexReunion > 0) {
                        descripcionLimpia = descripcionLimpia.substring(0, indexReunion).trim();
                    }
                    
                    // Añadir la información de la reunión actualizada
                    String nuevaDescripcion = descripcionLimpia + "\n\n----- REUNIÓN DE GOOGLE MEET -----\n" +
                                          "Enlace de la reunión: " + meetLink + 
                                          "\nFecha: " + fechaReunion + " a las " + horaReunion + 
                                          "\nDuración: " + duracionReunion + " minutos";
                    tarea.setDescripcion(nuevaDescripcion);
                } else if (tieneReunionExistente) {
                    redirectAttributes.addFlashAttribute("mensajeAdvertencia", 
                        "Tarea actualizada pero hubo un problema al actualizar la reunión.");
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("mensajeError", 
                    "Tarea actualizada pero ocurrió un error con la reunión: " + e.getMessage());
            }
        } else if (editarReunion == null && tareaExistente.getDescripcion() != null && 
                  tareaExistente.getDescripcion().contains("REUNIÓN DE GOOGLE MEET")) {
            // Si había una reunión pero ahora no se quiere, eliminar la parte de la reunión
            String descripcionLimpia = tarea.getDescripcion();
            int indexReunion = descripcionLimpia.indexOf("----- REUNIÓN DE GOOGLE MEET -----");
            if (indexReunion > 0) {
                descripcionLimpia = descripcionLimpia.substring(0, indexReunion).trim();
                tarea.setDescripcion(descripcionLimpia);
                
                // Extraer el ID del evento para eliminarlo
                com.usta.startupconnect.dto.MeetingInfo meetingInfo = 
                    googleMeetService.extractMeetingInfoFromDescription(tareaExistente.getDescripcion());
                
                if (meetingInfo != null && meetingInfo.getEventId() != null) {
                    try {
                        boolean deleted = googleMeetService.deleteEvent(meetingInfo.getEventId());
                        if (deleted) {
                            redirectAttributes.addFlashAttribute("mensajeExito", 
                                "Tarea actualizada y reunión eliminada exitosamente.");
                        } else {
                            redirectAttributes.addFlashAttribute("mensajeAdvertencia", 
                                "Tarea actualizada pero hubo un problema al eliminar la reunión.");
                        }
                    } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("mensajeError", 
                            "Error al eliminar la reunión: " + e.getMessage());
                    }
                }
            }
        } else {
            redirectAttributes.addFlashAttribute("mensajeExito", "Tarea actualizada exitosamente");
        }
        
        tareaService.save(tarea);
        return "redirect:/tarea";
    }@RequestMapping("/eliminarTarea/{id}")
    public String eliminarTarea(@PathVariable("id") Long idTarea, RedirectAttributes redirectAttributes) {
        try {
            // Buscar la tarea antes de eliminarla
            TareaEntity tarea = tareaService.findById(idTarea);
            
            if (tarea != null) {
                // Verificar si la tarea tiene una reunión asociada
                String descripcion = tarea.getDescripcion();
                if (descripcion != null && descripcion.contains("REUNIÓN DE GOOGLE MEET")) {
                    // Extraer el enlace de la reunión
                    int startIndex = descripcion.indexOf("Enlace de la reunión: ");
                    if (startIndex > 0) {
                        startIndex += "Enlace de la reunión: ".length();
                        int endIndex = descripcion.indexOf("\n", startIndex);
                        if (endIndex < 0) {
                            endIndex = descripcion.length();
                        }
                        
                        String meetLink = descripcion.substring(startIndex, endIndex).trim();
                        
                        // Extraer el ID del evento y eliminarlo
                        String eventId = googleMeetService.extractEventIdFromLink(meetLink);
                        if (eventId != null) {
                            boolean deleted = googleMeetService.deleteEvent(eventId);
                            if (deleted) {
                                System.out.println("Reunión eliminada con éxito: " + eventId);
                            } else {
                                System.out.println("No se pudo eliminar la reunión: " + eventId);
                            }
                        }
                    }
                }
                
                // Eliminar la tarea
                tareaService.deleteById(idTarea);
                redirectAttributes.addFlashAttribute("mensajeExito", "Tarea eliminada exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", "No se encontró la tarea");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al eliminar la tarea: " + e.getMessage());
        }
        
        return "redirect:/tarea";
    }

    @GetMapping(value = "/verTarea/{id}")
    public String verTarea(Model model, @PathVariable(value = "id") Long id) {
        TareaEntity tarea = tareaService.findById(id);
        model.addAttribute("title", "Ver tarea");
        model.addAttribute("tareaDetalle", tarea);
        
        // Obtener las entregas asociadas a esta tarea
        List<com.usta.startupconnect.entities.EntregableEntity> entregables = entregablesService.findByIdTarea(id);
        model.addAttribute("entregables", entregables);
        
        return "tarea/detalleTarea";
    }
}
