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
    public String listaEtapas(Model model) {
        model.addAttribute("title", "Etapas");
        model.addAttribute("urlRegistro", "/crearEtapa");
        List<EtapaEntity> lista = etapaService.findAll();
        lista.sort(Comparator.comparing(EtapaEntity::getId));
        model.addAttribute("etapas", lista);
        return "/etapa/listarEtapas";
    }

    @GetMapping(value = "/crearEtapa")
    public String crearEtapa(Model model) {
        model.addAttribute("title", "Crear Etapa");
        EtapaEntity etapa = new EtapaEntity();
        model.addAttribute("etapa", etapa);
        return "/etapa/crearEtapa";
    }

    @PostMapping("/crearEtapa")
    public String crearEtapa(@Valid @ModelAttribute("etapa") EtapaEntity etapa,
            BindingResult result, @RequestParam("mentorId") String mentorId) {
        if (result.hasErrors()) {
            System.out.println(result.getFieldErrors());
            return "etapa/crearEtapa";
        }

        MentorEntity mentor = mentorService.findById(mentorId);
        if (mentor == null) {

            result.rejectValue("mentor", "error.mentor", "El mentor especificado no existe");
            return "etapa/crearEtapa";
        }

        etapa.setMentor(mentor);

        etapaService.save(etapa);
        System.out.println("Etapa guardada correctamente");

        
        return "redirect:/etapa";
    }

    @GetMapping(value = "/editarEtapa/{id}")
    public String editarEtapa(Model model, @PathVariable(value = "id") Long id) {
        EtapaEntity etapa = etapaService.findById(id);
        model.addAttribute("title", "Editar etapa");
        model.addAttribute("etapaEditar", etapa);
        return "etapa/editarEtapa";
    }

    @PostMapping("/editarEtapa/{id}")
    public String editarEtapa(@Valid EtapaEntity etapa,
            BindingResult result,
            RedirectAttributes redirectAttributes, @RequestParam("mentorId") String mentorId) {

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
        redirectAttributes.addFlashAttribute("mensajeExito", "etapa actualizado exitosamente");
        return "redirect:/etapa";
    }

    @RequestMapping("/eliminarEtapa/{id}")
    public String eliminarEtapa(@PathVariable("id") Long idEtapa) {
        etapaService.deleteById(idEtapa);
        return "redirect:/etapa";
    }    @GetMapping(value = "/verEtapa/{id}")
    public String verEtapa(Model model, @PathVariable(value = "id") Long id) {
        EtapaEntity etapa = etapaService.findById(id);
        model.addAttribute("title", "Ver etapa");
        model.addAttribute("etapaDetalle", etapa);
        
        // Obtener las tareas asociadas a esta etapa
        List<TareaEntity> tareasAsociadas = tareaService.findByEtapa(etapa);
        model.addAttribute("tareas", tareasAsociadas);
        
        return "etapa/detalleEtapa";
    }
}
