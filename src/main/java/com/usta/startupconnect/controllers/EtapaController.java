package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.TareaEntity;
import com.usta.startupconnect.models.services.EtapaService;
import com.usta.startupconnect.models.services.MentorService;
import com.usta.startupconnect.models.services.TareaService;

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

import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

@Controller
public class EtapaController {    @Autowired
    private EtapaService etapaService;

    @Autowired
    private MentorService mentorService;
      @Autowired
    private TareaService tareaService;

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
            // Si es mentor, mostrar solo sus etapas
            String email = authentication.getName();
            MentorEntity mentor = mentorService.findByUsuarioEmail(email);
            
            if (mentor != null) {
                lista = etapaService.findByMentor(mentor);
            } else {
                // Si no se encuentra el mentor, mostrar lista vacía
                lista = java.util.Collections.emptyList();
            }
        }
        
        lista.sort(Comparator.comparing(EtapaEntity::getId));
        model.addAttribute("etapas", lista);
        return "/etapa/listarEtapas";
    }    @GetMapping(value = "/crearEtapa")
    public String crearEtapa(Model model, org.springframework.security.core.Authentication authentication) {
        // Verificar si el usuario es administrador o mentor
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isMentor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MENTOR"));
                
        if (!isAdmin && !isMentor) {
            // Si no es administrador ni mentor, redirigir a la lista de etapas
            return "redirect:/etapa";
        }
        
        model.addAttribute("title", "Crear Etapa");
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
    }@PostMapping("/crearEtapa")
    public String crearEtapa(@Valid @ModelAttribute("etapa") EtapaEntity etapa,
            BindingResult result, @RequestParam("mentorId") String mentorId,
            org.springframework.security.core.Authentication authentication) {
            
        // Verificar si el usuario es administrador o mentor
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isMentor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MENTOR"));
                
        if (!isAdmin && !isMentor) {
            // Si no es administrador ni mentor, redirigir a la lista de etapas
            return "redirect:/etapa";
        }
        
        if (result.hasErrors()) {
            System.out.println(result.getFieldErrors());
            return "etapa/crearEtapa";
        }

        MentorEntity mentor;
        
        // Si es mentor, asignarle la etapa a sí mismo
        if (isMentor && !isAdmin) {
            String email = authentication.getName();
            mentor = mentorService.findByUsuarioEmail(email);
            if (mentor == null) {
                result.rejectValue("mentor", "error.mentor", "No se pudo encontrar el perfil de mentor asociado");
                return "etapa/crearEtapa";
            }
        } else {
            // Si es administrador, usar el mentor seleccionado
            mentor = mentorService.findById(mentorId);
            if (mentor == null) {
                result.rejectValue("mentor", "error.mentor", "El mentor especificado no existe");
                return "etapa/crearEtapa";
            }        }

        etapa.setMentor(mentor);

        etapaService.save(etapa);
        System.out.println("Etapa guardada correctamente");

        
        return "redirect:/etapa";
    }    @GetMapping(value = "/editarEtapa/{id}")
    public String editarEtapa(Model model, @PathVariable(value = "id") Long id, org.springframework.security.core.Authentication authentication) {
        // Verificar si el usuario es administrador o mentor
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isMentor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MENTOR"));
                
        if (!isAdmin && !isMentor) {
            // Si no es administrador ni mentor, redirigir a la lista de etapas
            return "redirect:/etapa";
        }
        
        EtapaEntity etapa = etapaService.findById(id);
        
        // Si es mentor, verificar que la etapa le pertenezca
        if (isMentor && !isAdmin) {
            String email = authentication.getName();
            MentorEntity mentor = mentorService.findByUsuarioEmail(email);
            
            if (mentor == null || !mentor.getDocumento().equals(etapa.getMentor().getDocumento())) {
                // Si no es el mentor asignado, redirigir a la lista de etapas
                return "redirect:/etapa";
            }
        }
        
        model.addAttribute("title", "Editar etapa");
        model.addAttribute("etapaEditar", etapa);
        // Agregar el ID del mentor actual para el formulario
        model.addAttribute("mentorId", etapa.getMentor().getDocumento());
        return "etapa/editarEtapa";
    }    @PostMapping("/editarEtapa/{id}")
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
            // Si no es administrador ni mentor, redirigir a la lista de etapas
            return "redirect:/etapa";
        }

        // Si es mentor, verificar que esté editando su propia etapa
        if (isMentor && !isAdmin) {
            // Obtener la etapa original
            EtapaEntity etapaOriginal = etapaService.findById(etapa.getId());
            if (etapaOriginal == null) {
                return "redirect:/etapa";
            }
            
            // Verificar que sea el mentor asignado
            String email = authentication.getName();
            MentorEntity mentor = mentorService.findByUsuarioEmail(email);
            
            if (mentor == null || !mentor.getDocumento().equals(etapaOriginal.getMentor().getDocumento())) {
                // Si no es el mentor asignado, redirigir a la lista de etapas
                return "redirect:/etapa";
            }
            
            // Asegurar que no se cambie el mentor asignado
            mentorId = mentor.getDocumento();
        }

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "etapa/editarEtapa";
        }

        // Buscar el mentor por ID
        MentorEntity mentor = mentorService.findById(mentorId);
        if (mentor == null) {
            result.rejectValue("mentor", "error.mentor", "El mentor especificado no existe");
            return "etapa/editarEtapa";
        }

        etapa.setMentor(mentor);

        etapaService.save(etapa);
        redirectAttributes.addFlashAttribute("mensajeExito", "etapa actualizada exitosamente");
        return "redirect:/etapa";
    }    @RequestMapping("/eliminarEtapa/{id}")
    public String eliminarEtapa(@PathVariable("id") Long idEtapa, org.springframework.security.core.Authentication authentication) {
        // Verificar si el usuario es administrador o mentor
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isMentor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MENTOR"));
                
        if (!isAdmin && !isMentor) {
            // Si no es administrador ni mentor, redirigir a la lista de etapas
            return "redirect:/etapa";
        }
        
        // Si es mentor, verificar que la etapa le pertenezca
        if (isMentor && !isAdmin) {
            EtapaEntity etapa = etapaService.findById(idEtapa);
            if (etapa == null) {
                return "redirect:/etapa";
            }
            
            String email = authentication.getName();
            MentorEntity mentor = mentorService.findByUsuarioEmail(email);
            
            if (mentor == null || !mentor.getDocumento().equals(etapa.getMentor().getDocumento())) {
                // Si no es el mentor asignado, redirigir a la lista de etapas
                return "redirect:/etapa";
            }
        }
        
        etapaService.deleteById(idEtapa);
        return "redirect:/etapa";
    }@GetMapping(value = "/verEtapa/{id}")
    public String verEtapa(Model model, @PathVariable(value = "id") Long id, org.springframework.security.core.Authentication authentication) {
        EtapaEntity etapa = etapaService.findById(id);
        
        // Verificar si el usuario tiene permiso para ver esta etapa
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        if (!isAdmin) {
            // Si es mentor, verificar que la etapa le pertenezca
            String email = authentication.getName();
            MentorEntity mentor = mentorService.findByUsuarioEmail(email);
            
            if (mentor == null || !mentor.getDocumento().equals(etapa.getMentor().getDocumento())) {
                // Si no es el mentor asignado, redirigir a la lista de etapas
                return "redirect:/etapa";
            }
        }
        
        model.addAttribute("title", "Ver etapa");
        model.addAttribute("etapaDetalle", etapa);
        
        // Obtener las tareas asociadas a esta etapa
        List<TareaEntity> tareasAsociadas = tareaService.findByEtapa(etapa);
        model.addAttribute("tareas", tareasAsociadas);
        
        return "etapa/detalleEtapa";
    }
}
