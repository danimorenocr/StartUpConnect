package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.models.services.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class MentorController {

    @Autowired
    private MentorService mentorService;

    @GetMapping(value = "/mentor")
    public String listarMentores(Model model) {
        model.addAttribute("title", "Mentores");
        model.addAttribute("urlRegistro", "/crearMentor");
        List<MentorEntity> lista = mentorService.findAll();
        lista.sort(Comparator.comparing(MentorEntity::getDocumento));
        model.addAttribute("mentores", lista);
        return "/mentor/listarMentores";
    }
}
