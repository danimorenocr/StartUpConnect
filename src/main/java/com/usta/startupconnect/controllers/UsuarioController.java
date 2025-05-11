package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = "/administrador")
    public String administrador(Model model) {
        return "/administrador/dashboardAdmin";
    }

    @GetMapping(value = "/usuario")
    public String listarUsuarios(Model model) {
        model.addAttribute("title", "Usuarios");
        model.addAttribute("urlRegistro", "/crearUsuarios");
        List<UsuarioEntity> lista = usuarioService.findAll();
        lista.sort(Comparator.comparing(UsuarioEntity::getDocumento));
        model.addAttribute("usuarios", lista);
        return "/usuario/listarUsuarios";
    }

}
