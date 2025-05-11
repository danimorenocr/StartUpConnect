package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.TareaEntity;
import com.usta.startupconnect.models.services.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class TareaController {
    @Autowired
    private TareaService tareaService;


    @GetMapping(value = "/tarea")
    public String listaTareas(Model model) {
        model.addAttribute("title", "Tareas");
        model.addAttribute("urlRegistro", "/crearTarea");
        List<TareaEntity> lista = tareaService.findAll();
        lista.sort(Comparator.comparing(TareaEntity::getId));
        model.addAttribute("tareas", lista);
        return "/tarea/listarTareas";
    }
}
