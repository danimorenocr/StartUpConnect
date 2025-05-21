package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.RolEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.RolService;
import com.usta.startupconnect.models.services.UsuarioService;
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

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RolService rolService;

    @GetMapping(value = "/administrador")
    public String administrador(Model model) {
        return "/administrador/dashboardAdmin";
    }

    @GetMapping(value = "/usuario")
    public String listarUsuarios(Model model) {
        model.addAttribute("title", "Usuarios");
        model.addAttribute("crearUsuario", "/crearUsuario");
        List<UsuarioEntity> lista = usuarioService.findAll();
        lista.sort(Comparator.comparing(UsuarioEntity::getDocumento));
        model.addAttribute("usuarios", lista);
        return "/usuario/listarUsuarios";
    }

    @GetMapping(value = "/crearUsuario")
    public String crearUsuario(Model model) {
        List<RolEntity> roles = rolService.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("title", "Crear usuario");
        model.addAttribute("usuario", new UsuarioEntity());
        return "/usuario/formCrearUsuario";
    }

    @PostMapping(value = "/crearUsuario")
    public String guardarUsuario(@Valid UsuarioEntity usuario, @RequestParam(value = "foto") MultipartFile foto,
            BindingResult result, RedirectAttributes redirectAttributes) {
        String urlImagen = guardarImagen(foto);
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "usuario/crearUsuario";

        }

        usuario.setFotoUrl(urlImagen);
        usuario.setFecha_creacion(LocalDate.now());
        usuarioService.save(usuario);

        // Redirige según el rol
        Long rolId = usuario.getRol().getIdRol();
        if (rolId == 2L) {
            return "redirect:/crearMentor";
        } else if (rolId == 3L) {
            return "redirect:/crearEmprendedor";
        }

        redirectAttributes.addFlashAttribute("mensajeExito", "Room Saved Successfully");
        return "redirect:/usuario";
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

    @PostMapping(value = "/eliminarUsuario/{id}")
    public String eliminarHabitacion(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes) {
        System.out.println("Intentando eliminar habitación con ID: " + id);

        if (id > 0) {
            UsuarioEntity usuario = usuarioService.findById(id);
            if (usuario != null) {
                System.out.println("usuario encontrada: " + usuario.getDocumento());

                usuarioService.deleteById(id);
                System.out.println("usuario eliminada correctamente.");
                redirectAttributes.addFlashAttribute("success", "user deleted successfully");
            } else {
                System.out.println("Error: No se encontró la usuario con ID: " + id);
                redirectAttributes.addFlashAttribute("error", "user not found");
            }
        } else {
            System.out.println("Error: ID inválido.");
            redirectAttributes.addFlashAttribute("error", "Invalid ID");
        }

        return "redirect:/usuario";
    }

    @GetMapping(value = "/editarUsuario/{id}")
    public String editarUsuario(Model model, @PathVariable(value = "id") Long id) {
        UsuarioEntity usuario = usuarioService.findById(id);
        List<RolEntity> roles = rolService.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("title", "Editar Usuario");
        model.addAttribute("usuarioEditar", usuario);
        model.addAttribute("imagen", usuario.getFotoUrl());
        return "usuario/editarUsuario";
    }

    @PostMapping("/editarUsuario/{id}")
    public String actualizarUsuario(@Valid UsuarioEntity usuario,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "usuario/editarUsuario";
        }

        Long documento = Long.parseLong(usuario.getDocumento());
        UsuarioEntity usuarioExistente = usuarioService.findById(documento);

        if (usuarioExistente != null) {
            usuario.setFecha_creacion(usuarioExistente.getFecha_creacion());
        } else {
            usuario.setFecha_creacion(LocalDate.now());
        }

        if (foto != null && !foto.isEmpty()) {
            String urlImagen = guardarImagen(foto);
            usuario.setFotoUrl(urlImagen);
        } else if (usuarioExistente != null) {
            usuario.setFotoUrl(usuarioExistente.getFotoUrl());
        }

        usuarioService.save(usuario);
        redirectAttributes.addFlashAttribute("mensajeExito", "Usuario actualizado exitosamente");
        return "redirect:/usuario";
    }

    @GetMapping(value = "/verUsuario/{id}")
    public String verUsuario(Model model, @PathVariable(value = "id") Long id) {
        UsuarioEntity usuario = usuarioService.findById(id);
        List<RolEntity> roles = rolService.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("title", "Editar Usuario");
        model.addAttribute("usuarioEditar", usuario);
        model.addAttribute("imagen", usuario.getFotoUrl());
        return "usuario/detalleUsuario";
    }

}
