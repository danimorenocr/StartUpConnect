package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.EmprendedorService;
import com.usta.startupconnect.models.services.StartupService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

@Controller
public class StartupController {
    @Autowired
    private StartupService startupService;

    @Autowired
    private EmprendedorService emprendedorService;

    @GetMapping(value = "/startup")
    public String listarStartups(Model model) {
        model.addAttribute("title", "Startups");
        model.addAttribute("urlRegistro", "/crearStartup");
        List<StartupEntity> lista = startupService.findAll();
        lista.sort(Comparator.comparing(StartupEntity::getId));
        model.addAttribute("startups", lista);
        return "/startup/listarStartups";
    }

    @GetMapping(value = "/crearStartup")
    public String crearStartup(Model model) {
        model.addAttribute("title", "Crear startup");
        model.addAttribute("startup", new StartupEntity());
        return "/startup/crearStartup";
    }

    @PostMapping(value = "/eliminarStartup/{id}")
    public String eliminarStartup(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes) {
        if (id <= 0) {
            redirectAttributes.addFlashAttribute("error", "ID inválido");
            return "redirect:/startup";
        }

        EmprendedorEntity emprendedor = emprendedorService.findById(id);
        if (emprendedor == null) {
            redirectAttributes.addFlashAttribute("error", "emprendedor no encontrado");
            return "redirect:/startup";
        }

        emprendedorService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "emprendedor eliminado correctamente");

        return "redirect:/startup";
    }
}
