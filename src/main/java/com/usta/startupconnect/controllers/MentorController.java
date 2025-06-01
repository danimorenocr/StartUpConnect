package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.TareaEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.FeedbackEntity;
import com.usta.startupconnect.models.services.MentorService;
import com.usta.startupconnect.models.services.UsuarioService;
import com.usta.startupconnect.models.services.TareaService;
import com.usta.startupconnect.models.dao.EtapaDao;
import com.usta.startupconnect.models.dao.FeedbackDao;
import com.usta.startupconnect.security.JpaUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.Calendar;

import javax.validation.Valid;

@Controller
public class MentorController {

    @Autowired
    private MentorService mentorService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TareaService tareaService;
    @Autowired
    private EtapaDao etapaDao;
    @Autowired
    private FeedbackDao feedbackDao;
    @Autowired
    private JpaUserDetailsService userDetailsService;
    
    @GetMapping(value = "/dashboardMentor")
    public String dashboardMentor(Model model) {
        // Obtener el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Buscar el usuario por email (username es el email en este sistema)
        UsuarioEntity usuario = usuarioService.findByEmail(username);
        if (usuario == null) {
            return "redirect:/login";
        }
        
        // Buscar el mentor asociado al usuario
        MentorEntity mentor = mentorService.findById(usuario.getDocumento());
        if (mentor == null) {
            return "redirect:/login";
        }
        
        // Nombre para el banner de bienvenida
        model.addAttribute("nombreCompleto", usuario.getNombreUsu());
        
        // Obtener las etapas del mentor
        List<EtapaEntity> etapas = etapaDao.findByMentor(mentor);
        model.addAttribute("totalEtapas", etapas.size());
        
        // Obtener tareas de las etapas del mentor
        List<TareaEntity> tareas = etapas.stream()
                .flatMap(etapa -> tareaService.findByEtapa(etapa).stream())
                .collect(Collectors.toList());
        model.addAttribute("totalTareas", tareas.size());
        
        // Etapas activas (que están en curso actualmente)
        Date hoy = new Date();
        List<EtapaEntity> etapasActivas = etapas.stream()
                .filter(etapa -> etapa.getFechaInicio().before(hoy) && etapa.getFechaFin().after(hoy))
                .collect(Collectors.toList());
        model.addAttribute("etapasActivas", etapasActivas.size());
        
        // Tareas próximas a vencer (en los próximos 7 días)
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_MONTH, 7);
        Date proximos7Dias = calendario.getTime();
        
        List<TareaEntity> tareasProximasAVencer = tareas.stream()
                .filter(tarea -> tarea.getFechaEntrega().after(hoy) && tarea.getFechaEntrega().before(proximos7Dias))
                .collect(Collectors.toList());
        model.addAttribute("tareasProximasAVencer", tareasProximasAVencer);
        
        // Estadísticas de etapas por estado
        Map<String, Integer> etapasPorEstado = new HashMap<>();
        etapasPorEstado.put("Activas", etapasActivas.size());
        etapasPorEstado.put("Completadas", (int) etapas.stream()
                .filter(etapa -> etapa.getFechaFin().before(hoy))
                .count());
        etapasPorEstado.put("Por iniciar", (int) etapas.stream()
                .filter(etapa -> etapa.getFechaInicio().after(hoy))
                .count());
        model.addAttribute("etapasPorEstado", etapasPorEstado);
        
        // Próxima etapa a iniciar
        etapas.stream()
                .filter(etapa -> etapa.getFechaInicio().after(hoy))
                .min(Comparator.comparing(EtapaEntity::getFechaInicio))
                .ifPresent(proximaEtapa -> model.addAttribute("proximaEtapa", proximaEtapa));
        
        return "/mentor/dashboardMentor";
    }

    @GetMapping(value = "/mentor")
    public String listarMentores(Model model) {
        model.addAttribute("title", "Mentores");
        model.addAttribute("urlRegistro", "/crearMentor");
        List<MentorEntity> lista = mentorService.findAll();
        lista.sort(Comparator.comparing(MentorEntity::getDocumento));
        model.addAttribute("mentores", lista);
        return "/mentor/listarMentores";
    }

    @GetMapping(value = "/crearMentor")
    public String crearMentor(Model model) {
        model.addAttribute("title", "Crear mentor");
        model.addAttribute("mentor", new MentorEntity());
        return "/mentor/crearMentor";
    }

    @PostMapping(value = "/crearMentor")
    public String guardarMentor(@Valid MentorEntity mentor, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "mentor/crearMentor";
        }

        try {
            String documentoUsuario = mentor.getDocumento();

            UsuarioEntity usuario = usuarioService.findById(documentoUsuario);

            if (usuario == null) {
                redirectAttributes.addFlashAttribute("error", "No existe un usuario con el documento especificado");
                return "redirect:/crearMentor";
            }

            mentor.setUsuario(usuario);

            mentorService.save(mentor);
            redirectAttributes.addFlashAttribute("mensajeExito", "Mentor guardado exitosamente");
            return "redirect:/mentor";
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "El documento debe ser un número válido");
            return "redirect:/crearMentor";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el mentor: " + e.getMessage());
            return "redirect:/crearMentor";
        }
    }

    @PostMapping(value = "/eliminarMentor/{id}")
    public String eliminarMentor(@PathVariable(value = "id") String id, RedirectAttributes redirectAttributes) {
        try {
            if (id == null || id.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "ID inválido");
                return "redirect:/mentor";
            }

            MentorEntity mentor = mentorService.findById(id);

            if (mentor == null) {
                redirectAttributes.addFlashAttribute("error", "Mentor no encontrado");
                return "redirect:/mentor";
            }

            mentorService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Mentor eliminado correctamente");

            return "redirect:/mentor";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el mentor: " + e.getMessage());
            return "redirect:/mentor";
        }
    }

    @GetMapping(value = "/verMentor/{id}")
    public String verEmprendedor(Model model, @PathVariable(value = "id") String id) {
        MentorEntity mentor = mentorService.findById(id);
        model.addAttribute("title", "Ver Mentor");
        model.addAttribute("mentorDetalle", mentor);
        return "mentor/detalleMentor";
    }

    @GetMapping(value = "/editarMentor/{id}")
    public String editarMentor(Model model, @PathVariable(value = "id") String id) {
        MentorEntity mentor = mentorService.findById(id);
        model.addAttribute("title", "Editar mentor");
        model.addAttribute("mentorEditar", mentor);
        return "mentor/editarMentor";
    }    @PostMapping("/editarMentor/{id}")
    public String actualizarMentor(@Valid MentorEntity mentor,
            BindingResult result,
            @PathVariable("id") String id,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "mentor/editarMentor";
        }

        MentorEntity mentorExistente = mentorService.findById(id);
        if (mentorExistente == null) {
            redirectAttributes.addFlashAttribute("error", "Emprendedor no encontrado");
            return "redirect:/mentor";
        }

        mentor.setUsuario(mentorExistente.getUsuario());

        mentorService.save(mentor);

        redirectAttributes.addFlashAttribute("mensajeExito", "Emprendedor actualizado exitosamente");
        return "redirect:/mentor";
    }
      @GetMapping(value = "/misStartupsAsignadas")
    public String listarStartupsMentor(Model model) {
        // Obtener el usuario autenticado
        UsuarioEntity usuario = userDetailsService.obtenerUsuarioAutenticado();
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        // Buscar el mentor asociado al usuario
        MentorEntity mentor = mentorService.findById(usuario.getDocumento());
        if (mentor == null) {
            return "redirect:/login";
        }
        
        // Obtener los feedbacks del mentor
        List<FeedbackEntity> feedbacks = feedbackDao.findByMentor(mentor);
        
        // Extraer las startups de los feedbacks
        List<StartupEntity> startups = feedbacks.stream()
                .map(FeedbackEntity::getStartup)
                .distinct()
                .collect(Collectors.toList());
        
        model.addAttribute("title", "Mis Startups Asignadas");
        model.addAttribute("startups", startups);
        model.addAttribute("mentor", mentor);
        
        return "startup/listarStartupsMentor";
    }

}
