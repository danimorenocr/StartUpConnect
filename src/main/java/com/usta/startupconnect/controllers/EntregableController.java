package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EntregableEntity;
import com.usta.startupconnect.models.services.EntregablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class EntregableController {
    @Autowired
    private EntregablesService entregablesService;

    @GetMapping(value = "/entregable")
    public String listarEtregables(Model model) {
        model.addAttribute("title", "Etregables");
        model.addAttribute("urlRegistro", "/crearEtregable");
        List<EntregableEntity> lista = entregablesService.findAll();
        lista.sort(Comparator.comparing(EntregableEntity::getId));
        model.addAttribute("entregables", lista);
        return "/entregable/listarEntregables";
    }

    @GetMapping(value = "/crearEntregable")
    public String crearEntregable(Model model) {
        model.addAttribute("title", "Crear Entregable");
        EntregableEntity entregable = new EntregableEntity();
        model.addAttribute("entregable", entregable);
        return "/entregable/crearEntregable";
    }

}
