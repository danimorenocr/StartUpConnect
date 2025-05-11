package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.models.services.EtapaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class EtapaController {
    @Autowired
    private EtapaService etapaService;

    @GetMapping(value = "/etapa")
    public String listaEtapas(Model model) {
        model.addAttribute("title", "Etapas");
        model.addAttribute("urlRegistro", "/crearEtapa");
        List<EtapaEntity> lista = etapaService.findAll();
        lista.sort(Comparator.comparing(EtapaEntity::getId));
        model.addAttribute("etapas", lista);
        return "/etapa/listarEtapas";
    }
}
