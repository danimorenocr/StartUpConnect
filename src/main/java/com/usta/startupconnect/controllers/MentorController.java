package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.MentorService;
import com.usta.startupconnect.models.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
        return "/mentor/CrearMentor";
    }

    // @PostMapping(value = "/crearMentor")
    // public String guardarMentor(@Valid MentorEntity mentor, BindingResult result,
    //         RedirectAttributes redirectAttributes) {
    //     if (result.hasErrors()) {
    //         System.out.println(result.getAllErrors());
    //         return "/mentor/crearMentor";
    //     }

    //     Long documentoUsuario = null;
    //     try {
    //         documentoUsuario = Long.parseLong(mentor.getDocumento());

    //         UsuarioEntity usuario = usuarioService.findById(documentoUsuario);

    //         if (usuario == null) {
    //             redirectAttributes.addFlashAttribute("error", "No existe un usuario con el documento especificado");
    //             return "redirect:/crearMentor";
    //         }

    //         mentor.setUsuario(usuario);

    //         mentorService.save(mentor);
    //         redirectAttributes.addFlashAttribute("mensajeExito", "Mentor guardado exitosamente");
    //         return "redirect:/mentor";
    //     } catch (NumberFormatException e) {
    //         redirectAttributes.addFlashAttribute("error", "El documento debe ser un número válido");
    //         return "redirect:/crearMentor";
    //     } catch (Exception e) {
    //         redirectAttributes.addFlashAttribute("error", "Error al guardar el mentor: " + e.getMessage());
    //         return "redirect:/crearMentor";
    //     }
    // }
}
