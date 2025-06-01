package com.usta.startupconnect.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.models.services.EmprendedorService;
import com.usta.startupconnect.models.services.StartupService;

@Controller
public class VitrinaController {
    @Autowired
    private EmprendedorService emprendedorService;

    @Autowired
    private StartupService startupService;

    @GetMapping("/vitrina")
    public String mostrarVitrina(Model model) {
        List<EmprendedorEntity> emprendedores = emprendedorService.findAll();
        List<StartupEntity> startups = startupService.findAll();

        model.addAttribute("emprendedores", emprendedores);
        model.addAttribute("startups", startups);
        model.addAttribute("title", "Vitrina de Startups");

        return "vitrina";
    }
}
