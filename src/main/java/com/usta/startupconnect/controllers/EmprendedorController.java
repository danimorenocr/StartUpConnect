package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.models.services.EmprendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class EmprendedorController {

    @Autowired
    private EmprendedorService emprendedorService;

    @GetMapping(value = "/emprendedor")
    public String listarEmprendedores(Model model) {
        model.addAttribute("title", "Emprendedores");
        model.addAttribute("urlRegistro", "/crearEmprendedor");
        List< EmprendedorEntity> lista = emprendedorService.findAll();
        lista.sort(Comparator.comparing(EmprendedorEntity::getDocumento));
        model.addAttribute("emprendedores", lista);
        return "/emprendedor/listarEmprendedores";
    }

}
