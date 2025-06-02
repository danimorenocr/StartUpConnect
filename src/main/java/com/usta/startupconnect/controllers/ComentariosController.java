package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.ComentariosEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.ComentariosService;
import com.usta.startupconnect.models.services.StartupService;
import com.usta.startupconnect.security.JpaUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
public class ComentariosController {

    @Autowired
    private ComentariosService comentariosService;

    @Autowired
    private StartupService startupService;

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @PostMapping("/startup/{id}/comentar")
    public String agregarComentario(@PathVariable("id") Long startupId,
                                  @RequestParam("comentario") String comentario,
                                  RedirectAttributes redirectAttributes) {
          // Obtener el usuario autenticado
        UsuarioEntity usuario = userDetailsService.obtenerUsuarioAutenticado();
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para comentar");
            return "redirect:/verStartup/" + startupId;
        }
          // Verificar que el usuario sea inversionista
        if (usuario.getRol() == null || !"INVERSIONISTA".equals(usuario.getRol().getRol().toUpperCase())) {
            redirectAttributes.addFlashAttribute("error", "Solo los inversionistas pueden comentar en las startups");
            return "redirect:/verStartup/" + startupId;
        }

        // Buscar la startup
        StartupEntity startup = startupService.findById(startupId);
        if (startup == null) {
            redirectAttributes.addFlashAttribute("error", "La startup no existe");
            return "redirect:/verStartup/" + startupId;
        }

        // Crear y guardar el comentario
        ComentariosEntity nuevoComentario = new ComentariosEntity();
        nuevoComentario.setComentario(comentario);
        nuevoComentario.setFechaComentario(LocalDateTime.now());
        nuevoComentario.setStartup(startup);
        nuevoComentario.setUsuario(usuario);

        comentariosService.save(nuevoComentario);

        redirectAttributes.addFlashAttribute("mensajeExito", "Comentario agregado exitosamente");
        return "redirect:/verStartup/" + startupId;
    }
}
