package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.FeedbackEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.TareaEntity;
import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.models.services.EtapaService;
import com.usta.startupconnect.models.services.MentorService;
import com.usta.startupconnect.models.services.FeedbackService;
import com.usta.startupconnect.models.services.TareaService;
import com.usta.startupconnect.models.services.StartupService;
import com.usta.startupconnect.models.services.EmprendedorService;
import com.usta.startupconnect.models.services.NotificacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.*;

@Controller
public class EtapaController {    
    @Autowired
    private EtapaService etapaService;

    @Autowired
    private MentorService mentorService;
    
    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private TareaService tareaService;

    @Autowired
    private StartupService startupService;

    @Autowired
    private EmprendedorService emprendedorService;

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping(value = "/etapa")
    public String listaEtapas(Model model, org.springframework.security.core.Authentication authentication) {
        model.addAttribute("title", "Etapas");
        model.addAttribute("urlRegistro", "/crearEtapa");

        List<EtapaEntity> lista;

        // Comprobar el rol del usuario autenticado
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // Si es administrador, mostrar todas las etapas
            lista = etapaService.findAll();
        } else {
            // Si es mentor, mostrar solo sus etapas a través de feedback
            String email = authentication.getName();
            MentorEntity mentor = mentorService.findByUsuarioEmail(email);

            if (mentor != null) {
                lista = feedbackService.findEtapasByMentor(mentor);
            } else {
                lista = java.util.Collections.emptyList();
            }
        }

        lista.sort(Comparator.comparing(EtapaEntity::getId));

        // Para cada etapa, obtenemos la startup asociada
        Map<Long, List<StartupEntity>> etapaStartups = new HashMap<>();
        for (EtapaEntity etapa : lista) {
            List<StartupEntity> startups = feedbackService.findStartupsByEtapa(etapa);
            etapaStartups.put(etapa.getId(), startups);
        }
        model.addAttribute("etapaStartups", etapaStartups);
        model.addAttribute("etapas", lista);
        
        return "/etapa/listarEtapas";
    }

    @GetMapping(value = "/crearEtapa")
    public String crearEtapa(Model model, @RequestParam(required = true) Long startupId,
            org.springframework.security.core.Authentication authentication) {
        // Verificar si el usuario es administrador o mentor
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isMentor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MENTOR"));

        if (!isAdmin && !isMentor) {
            return "redirect:/etapa";
        }

        // Obtener la startup seleccionada
        StartupEntity startup = startupService.findById(startupId);
        if (startup == null) {
            // Si no se encuentra la startup, redirigir a la lista de startups
            return "redirect:/misStartupsAsignadas";
        }

        model.addAttribute("title", "Crear Etapa");
        model.addAttribute("startup", startup);
        EtapaEntity etapa = new EtapaEntity();
        model.addAttribute("etapa", etapa);

        // Si es mentor, obtenemos su ID para pre-llenarlo en el formulario
        if (isMentor && !isAdmin) {
            String email = authentication.getName();
            MentorEntity mentor = mentorService.findByUsuarioEmail(email);
            if (mentor != null) {
                model.addAttribute("mentorId", mentor.getDocumento());
            }
        }

        return "/etapa/crearEtapa";
    }
    
    @PostMapping("/crearEtapa")
    public String crearEtapa(@Valid @ModelAttribute("etapa") EtapaEntity etapa,
            BindingResult result, 
            @RequestParam("mentorId") String mentorId,
            @RequestParam("startupId") Long startupId,
            RedirectAttributes redirectAttributes,
            org.springframework.security.core.Authentication authentication) {

        if (result.hasErrors()) {
            return "etapa/crearEtapa";
        }

        // Verificar si el usuario es administrador o mentor
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isMentor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MENTOR"));

        if (!isAdmin && !isMentor) {
            return "redirect:/etapa";
        }

        // Obtener el mentor
        MentorEntity mentor;
        if (isMentor && !isAdmin) {
            String email = authentication.getName();
            mentor = mentorService.findByUsuarioEmail(email);
        } else {
            mentor = mentorService.findById(mentorId);
        }

        if (mentor == null) {
            result.rejectValue("mentor", "error.mentor", "No se pudo encontrar el mentor");
            return "etapa/crearEtapa";
        }

        // Obtener la startup
        StartupEntity startup = startupService.findById(startupId);
        if (startup == null) {
            result.rejectValue("startup", "error.startup", "No se pudo encontrar la startup");
            return "etapa/crearEtapa";
        }

        try {
            // Guardar la etapa
            etapaService.save(etapa);

            // Crear el feedback para establecer la relación
            FeedbackEntity feedback = new FeedbackEntity();
            feedback.setMentor(mentor);
            feedback.setStartup(startup);
            feedback.setEtapa(etapa);
            feedback.setFechaCreacion(new Date());
            feedbackService.save(feedback);

            // Notificar al emprendedor asociado a la startup
            EmprendedorEntity emprendedor = startup.getEmprendedor();
            if (emprendedor != null && emprendedor.getUsuario() != null) {
                String mensaje = "Se ha creado una nueva etapa para tu startup: " + startup.getNombreStartup();
                notificacionService.notificarUsuario(emprendedor.getUsuario().getDocumento(), mensaje);
            }

            redirectAttributes.addFlashAttribute("mensajeExito", "Etapa creada exitosamente");
            return "redirect:/etapa";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la etapa: " + e.getMessage());
            return "etapa/crearEtapa";
        }
    }

    @GetMapping(value = "/editarEtapa/{id}")
    public String editarEtapa(Model model, @PathVariable(value = "id") Long id,
            org.springframework.security.core.Authentication authentication) {
        // Verificar si el usuario es administrador o mentor
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isMentor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MENTOR"));

        if (!isAdmin && !isMentor) {
            return "redirect:/etapa";
        }

        EtapaEntity etapa = etapaService.findById(id);
        if (etapa == null) {
            return "redirect:/etapa";
        }

        // Obtener el mentor a través de feedback
        MentorEntity mentor = feedbackService.findMentorByEtapa(etapa);

        // Si es mentor, verificar que la etapa le pertenezca
        if (isMentor && !isAdmin) {
            String email = authentication.getName();
            MentorEntity currentMentor = mentorService.findByUsuarioEmail(email);

            if (currentMentor == null || mentor == null || !mentor.getDocumento().equals(currentMentor.getDocumento())) {
                return "redirect:/etapa";
            }
        }

        model.addAttribute("title", "Editar etapa");
        model.addAttribute("etapaEditar", etapa);
        if (mentor != null) {
            model.addAttribute("mentorId", mentor.getDocumento());
        }
        return "etapa/editarEtapa";
    }

    @PostMapping("/editarEtapa/{id}")
    public String editarEtapa(@Valid EtapaEntity etapa,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            @RequestParam("mentorId") String mentorId,
            org.springframework.security.core.Authentication authentication) {

        // Verificar si el usuario es administrador o mentor
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isMentor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MENTOR"));

        if (!isAdmin && !isMentor) {
            return "redirect:/etapa";
        }

        if (result.hasErrors()) {
            return "etapa/editarEtapa";
        }

        // Obtener el mentor actual a través de feedback
        MentorEntity currentMentor = feedbackService.findMentorByEtapa(etapa);

        // Si es mentor, verificar que esté editando su propia etapa y no cambiar el mentor
        if (isMentor && !isAdmin) {
            String email = authentication.getName();
            MentorEntity userMentor = mentorService.findByUsuarioEmail(email);

            if (userMentor == null || currentMentor == null || 
                !currentMentor.getDocumento().equals(userMentor.getDocumento())) {
                return "redirect:/etapa";
            }

            // Mantener el mismo mentor
            mentorId = userMentor.getDocumento();
        }

        // Actualizar la etapa
        etapaService.save(etapa);

        // Si el mentor ha cambiado, actualizar el feedback
        MentorEntity newMentor = mentorService.findById(mentorId);
        if (newMentor != null && (currentMentor == null || 
            !newMentor.getDocumento().equals(currentMentor.getDocumento()))) {
            // Crear un nuevo feedback con el nuevo mentor
            FeedbackEntity feedback = new FeedbackEntity();
            feedback.setMentor(newMentor);
            feedback.setEtapa(etapa);
            feedback.setFechaCreacion(new Date());
            feedbackService.save(feedback);
        }

        redirectAttributes.addFlashAttribute("mensajeExito", "etapa actualizada exitosamente");
        return "redirect:/etapa";
    }

    @GetMapping(value = "/verEtapa/{id}")
    public String verEtapa(Model model, @PathVariable(value = "id") Long id,
            org.springframework.security.core.Authentication authentication) {
        EtapaEntity etapa = etapaService.findById(id);
        if (etapa == null) {
            return "redirect:/etapa";
        }

        // Obtener el mentor a través de feedback
        MentorEntity mentor = feedbackService.findMentorByEtapa(etapa);

        // Verificar si el usuario tiene permiso para ver esta etapa
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            // Si es mentor, verificar que la etapa le pertenezca
            String email = authentication.getName();
            MentorEntity currentMentor = mentorService.findByUsuarioEmail(email);

            if (currentMentor == null || mentor == null || 
                !mentor.getDocumento().equals(currentMentor.getDocumento())) {
                return "redirect:/etapa";
            }
        }

        model.addAttribute("title", "Ver etapa");
        model.addAttribute("etapaDetalle", etapa);
        model.addAttribute("mentor", mentor);

        // Obtener las tareas asociadas a esta etapa
        List<TareaEntity> tareasAsociadas = tareaService.findByEtapa(etapa);
        model.addAttribute("tareas", tareasAsociadas);

        return "etapa/detalleEtapa";
    }

    @RequestMapping("/eliminarEtapa/{id}")
    public String eliminarEtapa(@PathVariable("id") Long idEtapa,
            org.springframework.security.core.Authentication authentication) {
        // Verificar si el usuario es administrador o mentor
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isMentor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MENTOR"));

        if (!isAdmin && !isMentor) {
            return "redirect:/etapa";
        }

        EtapaEntity etapa = etapaService.findById(idEtapa);
        if (etapa == null) {
            return "redirect:/etapa";
        }

        // Obtener el mentor a través de feedback
        MentorEntity mentor = feedbackService.findMentorByEtapa(etapa);

        // Si es mentor, verificar que la etapa le pertenezca
        if (isMentor && !isAdmin) {
            String email = authentication.getName();
            MentorEntity currentMentor = mentorService.findByUsuarioEmail(email);

            if (currentMentor == null || mentor == null || 
                !mentor.getDocumento().equals(currentMentor.getDocumento())) {
                return "redirect:/etapa";
            }
        }

        etapaService.deleteById(idEtapa);
        return "redirect:/etapa";
    }

    @GetMapping("/etapasEmprendedor")
    public String listarEtapasEmprendedor(Model model, org.springframework.security.core.Authentication authentication) {
        // Verificar que el usuario sea un emprendedor
        boolean isEmprendedor = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_EMPRENDEDOR"));

        if (!isEmprendedor) {
            return "redirect:/";
        }

        // Obtener el emprendedor actual
        String email = authentication.getName();
        EmprendedorEntity emprendedor = emprendedorService.findByUsuarioEmail(email);

        if (emprendedor == null) {
            return "redirect:/";
        }

        // Obtener las startups del emprendedor
        List<StartupEntity> startups = startupService.findByEmprendedor(emprendedor);
        
        // Obtener etapas para todas las startups del emprendedor y sus mentores
        List<EtapaEntity> etapas = new ArrayList<>();
        Map<Long, MentorEntity> mentoresPorEtapa = new HashMap<>();
        Map<Long, List<TareaEntity>> tareasPorEtapa = new HashMap<>();
        
        for (StartupEntity startup : startups) {
            Collection<FeedbackEntity> feedbacks = feedbackService.findByStartup(startup);
            for (FeedbackEntity feedback : feedbacks) {
                if (feedback.getEtapa() != null) {
                    etapas.add(feedback.getEtapa());
                    mentoresPorEtapa.put(feedback.getEtapa().getId(), feedback.getMentor());
                    // Obtener las tareas asociadas a esta etapa
                    List<TareaEntity> tareas = tareaService.findByEtapa(feedback.getEtapa());
                    tareasPorEtapa.put(feedback.getEtapa().getId(), tareas);
                }
            }
        }

        model.addAttribute("etapas", etapas);
        model.addAttribute("mentoresPorEtapa", mentoresPorEtapa);
        model.addAttribute("tareasPorEtapa", tareasPorEtapa);
        model.addAttribute("title", "Mis Etapas");
        return "etapa/listarEtapasEmprendedor";
    }
}
