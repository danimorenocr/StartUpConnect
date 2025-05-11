package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EventoEntity;
import com.usta.startupconnect.models.services.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @GetMapping(value = "/evento")
    public String listarEventos(Model model) {
        model.addAttribute("title", "Eventos");
        model.addAttribute("urlRegistro", "/crearEvento");
        List<EventoEntity> lista = eventoService.findAll();
        lista.sort(Comparator.comparing(EventoEntity::getId));
        model.addAttribute("eventos", lista);
        return "/evento/listarEventos";
    }
    
    @GetMapping(value = "/crearEvento")
    public String crearEvento(Model model) {
        model.addAttribute("title", "Crear Evento");
        EventoEntity evento = new EventoEntity();
        model.addAttribute("evento", evento);
        return "/evento/crearEvento";
    }
}
