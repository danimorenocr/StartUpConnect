package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.StartupEntity;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    // @GetMapping(value = "/vitrina")
    // public String vitrina(Model model) {
    //     model.addAttribute("title", "Startups");
    //     model.addAttribute("urlRegistro", "/crearStartup");
    //     List<StartupEntity> lista = startupService.findAll();
    //     lista.sort(Comparator.comparing(StartupEntity::getId));
    //     model.addAttribute("startups", lista);
    //     return "/vitrina";
    // }

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

    @PostMapping("/crearStartup")
    public String crearStartup(@Valid @ModelAttribute("startup") StartupEntity startup,
            @RequestParam(value = "foto") MultipartFile foto,
            BindingResult result) {

        String urlImagen = guardarImagen(foto);

        if (result.hasErrors()) {
            System.out.println(result.getFieldErrors());
            return "startup/crearStartup";
        }

        String documentoEmprendedor = startup.getEmprendedor().getDocumento();

        EmprendedorEntity emprendedor = emprendedorService.findById(documentoEmprendedor);

        if (emprendedor == null) {

            System.out.println("Emprendedor no encontrado");
            System.out.println("Documento: " + documentoEmprendedor);
            System.out.println(emprendedor);
            return "redirect:/crearStartup?error=emprendedor_no_encontrado";
        }
        startup.setLogoUrl(urlImagen);
        startup.setFechaCreacion(LocalDate.now());
        startup.setEmprendedor(emprendedor);
        startupService.save(startup);
        return "redirect:/startup";
    }

    private String guardarImagen(MultipartFile imagen) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://api.imgbb.com/1/upload");

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("key", "dc172a14793a0bf9b87a96f1f2e5b4be", ContentType.TEXT_PLAIN);

            builder.addBinaryBody("image", imagen.getInputStream(), ContentType.DEFAULT_BINARY,
                    imagen.getOriginalFilename());

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (response.getStatusLine().getStatusCode() == 200) {
                String responseString = EntityUtils.toString(responseEntity);
                JSONObject jsonResponse = new JSONObject(responseString);
                boolean success = jsonResponse.getBoolean("success");

                if (success) {
                    JSONObject data = jsonResponse.getJSONObject("data");
                    return data.getString("url");
                } else {
                    System.err.println("Error loading image: " + jsonResponse.optString("error", "Unknown Error"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @GetMapping(value = "/editarStartup/{id}")
    public String editarStartup(Model model, @PathVariable(value = "id") Long id) {
        StartupEntity startup = startupService.findById(id);
        model.addAttribute("title", "Editar startup");
        model.addAttribute("startupEditar", startup);
        return "startup/editarStartup";
    }

    @PostMapping("/editarStartup/{id}")
    public String editarStartup(@Valid StartupEntity startup,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "startup/editarStartup";
        }
        Long id = startup.getId();
        StartupEntity startupExistente = startupService.findById(id);

        if (foto != null && !foto.isEmpty()) {
            String urlImagen = guardarImagen(foto);
            startup.setLogoUrl(urlImagen);
        } else if (startupExistente != null) {
            startup.setLogoUrl(startupExistente.getLogoUrl());
        }

        startupService.save(startup);
        redirectAttributes.addFlashAttribute("mensajeExito", "Evento actualizado exitosamente");
        return "redirect:/startup";
    }

    @GetMapping(value = "/verStartup/{id}")
    public String verStartup(Model model, @PathVariable(value = "id") Long id) {
        StartupEntity startup = startupService.findById(id);
        model.addAttribute("title", "Ver startup");
        model.addAttribute("startupDetalle", startup);
        return "startup/detalleStartup";
    }

    @RequestMapping("/eliminarStartup/{id}")
    public String eliminarStratup(@PathVariable("id") Long idStartup) {
        startupService.deleteById(idStartup);
        return "redirect:/startup";
    }

}
