package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.models.services.StartupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class StartupController {
    @Autowired
    private StartupService startupService;

    @GetMapping(value = "/startup")
    public String listarStartups(Model model) {
        model.addAttribute("title", "Startups");
        model.addAttribute("urlRegistro", "/crearStartup");
        List<StartupEntity> lista = startupService.findAll();
        lista.sort(Comparator.comparing(StartupEntity::getId));
        model.addAttribute("startups", lista);
        return "/startup/listarStartups";
    }
}
