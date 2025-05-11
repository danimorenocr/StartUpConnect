package com.usta.startupconnect.controllers;

import com.usta.startupconnect.models.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = "/administrador")
    public String administrador(Model model) {
        return "/administrador/dashboardAdmin";
    }

}
