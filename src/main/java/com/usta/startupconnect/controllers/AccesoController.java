package com.usta.startupconnect.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.usta.startupconnect.security.JpaUserDetailsService;

@Controller
public class AccesoController {
    
    @Autowired
    private JpaUserDetailsService userDetailsService;
    @GetMapping(value = "/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout, Model model) {

        model.addAttribute("title", "Iniciar Sesión");

        if (error != null) {
            model.addAttribute("error",
                    "Login incorrecto, comprueba nuevamente tu usuario y contraseña");
        }

        if (logout != null) {
            model.addAttribute("success", "Ha cerrado sesión correctamente");
        }

        return "login";
    }    @GetMapping(value = "/dashboard")
    public String dashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated()) {
            // Redirigir según el rol
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return "redirect:/administrador";
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MENTOR"))) {
                return "redirect:/mentor";
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPRENDEDOR"))) {
                return "redirect:/emprendedor";
            }
        }
        
        // Por defecto, redirige a la página principal
        return "redirect:/";
    }

}
