package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.MentorService;
import com.usta.startupconnect.models.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

@Controller
public class MentorController {

    @Autowired
    private MentorService mentorService;
    @Autowired
    private UsuarioService usuarioService;

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
    }

    @PostMapping("/editarMentor/{id}")
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

}
