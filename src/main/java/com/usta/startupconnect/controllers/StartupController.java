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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
        Long documentoEmprendedorLong = Long.parseLong(documentoEmprendedor);

        EmprendedorEntity emprendedor = emprendedorService.findByDocumento(documentoEmprendedorLong);

        if (emprendedor == null) {

            System.out.println("Emprendedor no encontrado");
            System.out.println("Documento: " + documentoEmprendedorLong);
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

}
