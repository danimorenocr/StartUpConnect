package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.EmprendedorService;
import com.usta.startupconnect.models.services.MentorService;
import com.usta.startupconnect.models.services.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

@Controller
public class PerfilController {

    private final Logger log = LoggerFactory.getLogger(PerfilController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MentorService mentorService;

    @Autowired
    private EmprendedorService emprendedorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Muestra la página de perfil del usuario autenticado
     */
    @GetMapping("/perfil")
    public String verPerfil(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            // Obtener el usuario por su email
            UsuarioEntity usuario = usuarioService.findByEmail(email);
            if (usuario == null) {
                return "redirect:/";
            }

            // Añadir el usuario al modelo
            model.addAttribute("usuario", usuario);

            // Determinar el rol del usuario
            String rolUsuario = "USUARIO";
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                rolUsuario = "ADMIN";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MENTOR"))) {
                rolUsuario = "MENTOR"; // Obtener datos de mentor
                MentorEntity mentor = mentorService.findById(usuario.getDocumento());
                if (mentor != null) {
                    model.addAttribute("mentor", mentor);
                }
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPRENDEDOR"))) {
                rolUsuario = "EMPRENDEDOR";
                // Obtener datos de emprendedor
                EmprendedorEntity emprendedor = emprendedorService.findByDocumento(usuario.getDocumento());
                if (emprendedor != null) {
                    model.addAttribute("emprendedor", emprendedor);
                }
            }

            model.addAttribute("rolUsuario", rolUsuario);
            model.addAttribute("title", "Mi Perfil");

            return "usuario/perfil";
        } catch (Exception e) {
            log.error("Error al cargar el perfil: ", e);
            return "redirect:/";
        }
    }

    /**
     * Muestra el formulario para editar el perfil
     */
    @GetMapping("/editarPerfil")
    public String editarPerfil(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            // Obtener el usuario por su email
            UsuarioEntity usuario = usuarioService.findByEmail(email);
            if (usuario == null) {
                return "redirect:/";
            }

            // Añadir el usuario al modelo
            model.addAttribute("usuario", usuario);

            // Determinar el rol del usuario
            String rolUsuario = "USUARIO";
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                rolUsuario = "ADMIN";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MENTOR"))) {
                rolUsuario = "MENTOR";
                // Obtener datos de mentor
                MentorEntity mentor = mentorService.findById(usuario.getDocumento());
                if (mentor != null) {
                    model.addAttribute("mentor", mentor);
                }
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPRENDEDOR"))) {
                rolUsuario = "EMPRENDEDOR";
                // Obtener datos de emprendedor
                EmprendedorEntity emprendedor = emprendedorService.findByDocumento(usuario.getDocumento());
                if (emprendedor != null) {
                    model.addAttribute("emprendedor", emprendedor);
                }
            }

            model.addAttribute("rolUsuario", rolUsuario);
            model.addAttribute("title", "Editar Perfil");

            return "usuario/editarPerfil";
        } catch (Exception e) {
            log.error("Error al cargar el formulario de editar perfil: ", e);
            return "redirect:/perfil";
        }
    }

    /**
     * Procesa la actualización del perfil de usuario
     */
    @PostMapping("/actualizarPerfil")
    public String actualizarPerfil(UsuarioEntity usuario,
            @RequestParam(value = "especialidad", required = false) String especialidad,
            @RequestParam(value = "biografia", required = false) String biografia,
            @RequestParam(value = "linkedin", required = false) String linkedin,
            @RequestParam(value = "anosExperiencia", required = false) Short anosExperiencia,
            @RequestParam(value = "universidad", required = false) String universidad,
            @RequestParam(value = "programaEducativo", required = false) String programaEducativo,
            RedirectAttributes flash) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            // Verificar que el usuario exista
            UsuarioEntity usuarioActual = usuarioService.findByEmail(email);
            if (usuarioActual == null) {
                flash.addFlashAttribute("error", "No se encontró el usuario");
                return "redirect:/perfil";
            }

            // Verificar si el correo ha cambiado
            boolean emailChanged = !usuarioActual.getEmailUsu().equals(usuario.getEmailUsu());
            
            // Actualizar datos básicos del usuario
            usuarioActual.setNombreUsu(usuario.getNombreUsu());
            usuarioActual.setEmailUsu(usuario.getEmailUsu());
            usuarioActual.setTelefono(usuario.getTelefono());

            // Guardar cambios del usuario
            usuarioService.save(usuarioActual);

            // Si el correo cambió, redirigir al login
            if (emailChanged) {
                flash.addFlashAttribute("success", "Perfil actualizado correctamente. Por favor, inicia sesión nuevamente con tu nuevo correo.");
                SecurityContextHolder.clearContext(); // Cerrar sesión
                return "redirect:/login";
            }

            // Actualizar información específica según el rol
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MENTOR"))) {
                MentorEntity mentor = mentorService.findById(usuarioActual.getDocumento());
                if (mentor != null && especialidad != null && biografia != null && linkedin != null
                        && anosExperiencia != null) {
                    mentor.setEspecialidad(especialidad);
                    mentor.setBiografia(biografia);
                    mentor.setLinkedin(linkedin);
                    mentor.setAnosExperiencia(anosExperiencia);
                    mentorService.save(mentor);
                }
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPRENDEDOR"))) {
                EmprendedorEntity emprendedor = emprendedorService.findByDocumento(usuarioActual.getDocumento());
                if (emprendedor != null && universidad != null && programaEducativo != null) {
                    emprendedor.setUniversidad(universidad);
                    emprendedor.setProgramaEducativo(programaEducativo);
                    emprendedorService.save(emprendedor);
                }
            }

            flash.addFlashAttribute("success", "Perfil actualizado correctamente");
            return "redirect:/perfil";
        } catch (Exception e) {
            log.error("Error al actualizar el perfil: ", e);
            flash.addFlashAttribute("error", "Error al actualizar el perfil");
            return "redirect:/editarPerfil";
        }
    }

    /**
     * Muestra el formulario para cambiar la contraseña
     */
    @GetMapping("/cambiarPassword")
    public String cambiarPassword(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            // Obtener el usuario por su email
            UsuarioEntity usuario = usuarioService.findByEmail(email);
            if (usuario == null) {
                return "redirect:/";
            }

            model.addAttribute("usuario", usuario);
            model.addAttribute("title", "Cambiar Contraseña");

            return "usuario/cambiarPassword";
        } catch (Exception e) {
            log.error("Error al cargar el formulario de cambiar contraseña: ", e);
            return "redirect:/perfil";
        }
    }

    /**
     * Procesa el cambio de contraseña
     */
    @PostMapping("/actualizarPassword")
    public String actualizarPassword(@RequestParam("documento") String documento,
            @RequestParam("passwordActual") String passwordActual,
            @RequestParam("passwordNueva") String passwordNueva,
            @RequestParam("confirmarPassword") String confirmarPassword,
            RedirectAttributes flash,
            Authentication authentication) {
        try {
            // Verificar que el usuario exista y coincida con el usuario autenticado
            UsuarioEntity usuario = usuarioService.findById(documento);
            if (usuario == null || !usuario.getEmailUsu().equals(authentication.getName())) {
                flash.addFlashAttribute("error", "No se encontró el usuario o no tienes permisos para realizar esta operación");
                return "redirect:/perfil";
            }

            // Verificar que la contraseña actual sea correcta
            if (!passwordEncoder.matches(passwordActual, usuario.getContrasenna())) {
                flash.addFlashAttribute("error", "La contraseña actual es incorrecta");
                return "redirect:/cambiarPassword";
            }

            // Verificar que las contraseñas nuevas coincidan
            if (!passwordNueva.equals(confirmarPassword)) {
                flash.addFlashAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/cambiarPassword";
            }

            // Verificar que la nueva contraseña no sea igual a la actual
            if (passwordEncoder.matches(passwordNueva, usuario.getContrasenna())) {
                flash.addFlashAttribute("error", "La nueva contraseña debe ser diferente a la actual");
                return "redirect:/cambiarPassword";
            }

            // Verificar la longitud mínima y requisitos de la contraseña
            if (passwordNueva.length() < 6) {
                flash.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres");
                return "redirect:/cambiarPassword";
            }

            // Actualizar la contraseña manteniendo la sesión del usuario
            String newHashedPassword = passwordEncoder.encode(passwordNueva);
            usuario.setContrasenna(newHashedPassword);
            usuarioService.save(usuario);

            // No cerrar la sesión, solo redirigir al perfil con mensaje de éxito
            flash.addFlashAttribute("success", "Contraseña actualizada correctamente");
            return "redirect:/perfil";
        } catch (Exception e) {
            log.error("Error al actualizar la contraseña: ", e);
            flash.addFlashAttribute("error", "Error al actualizar la contraseña");
            return "redirect:/cambiarPassword";
        }
    }

    /**
     * Muestra el formulario para cambiar la foto de perfil
     */
    @GetMapping("/cambiarFoto")
    public String cambiarFoto(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            // Obtener el usuario por su email
            UsuarioEntity usuario = usuarioService.findByEmail(email);
            if (usuario == null) {
                return "redirect:/";
            }

            model.addAttribute("usuario", usuario);
            model.addAttribute("title", "Cambiar Foto de Perfil");

            return "usuario/cambiarFoto";
        } catch (Exception e) {
            log.error("Error al cargar el formulario de cambiar foto: ", e);
            return "redirect:/perfil";
        }
    }    /**
     * Procesa el cambio de foto de perfil
     */
    @PostMapping("/actualizarFoto")
    public String actualizarFoto(@RequestParam("documento") String documento,
            @RequestParam("foto") MultipartFile foto,
            RedirectAttributes flash) {
        try {
            // Verificar que el usuario exista
            UsuarioEntity usuario = usuarioService.findById(documento);
            if (usuario == null) {
                flash.addFlashAttribute("error", "No se encontró el usuario");
                return "redirect:/perfil";
            }

            // Verificar que se haya seleccionado una foto
            if (foto.isEmpty()) {
                flash.addFlashAttribute("error", "Por favor seleccione una imagen");
                return "redirect:/cambiarFoto";
            }

            // Verificar que sea una imagen
            String contentType = foto.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                flash.addFlashAttribute("error", "El archivo debe ser una imagen");
                return "redirect:/cambiarFoto";
            }

            // Verificar el tamaño del archivo (máximo 5MB)
            if (foto.getSize() > 5 * 1024 * 1024) {
                flash.addFlashAttribute("error", "La imagen no debe superar los 5MB");
                return "redirect:/cambiarFoto";
            }

            try {
                // Upload to imgBB
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost("https://api.imgbb.com/1/upload");

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.addTextBody("key", "dc172a14793a0bf9b87a96f1f2e5b4be", ContentType.TEXT_PLAIN);
                builder.addBinaryBody("image", foto.getInputStream(), ContentType.DEFAULT_BINARY, foto.getOriginalFilename());

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
                        String imageUrl = data.getString("url");
                        usuario.setFotoUrl(imageUrl);
                        usuarioService.save(usuario);
                        flash.addFlashAttribute("success", "Foto de perfil actualizada correctamente");
                        return "redirect:/perfil";
                    } else {
                        log.error("Error al subir la imagen: " + jsonResponse.optString("error", "Error desconocido"));
                        flash.addFlashAttribute("error", "Error al subir la imagen");
                        return "redirect:/cambiarFoto";
                    }
                } else {
                    log.error("Error al subir la imagen: Código de respuesta " + response.getStatusLine().getStatusCode());
                    flash.addFlashAttribute("error", "Error al subir la imagen");
                    return "redirect:/cambiarFoto";
                }
            } catch (IOException e) {
                log.error("Error al subir la foto: ", e);
                flash.addFlashAttribute("error", "Error al subir la foto");
                return "redirect:/cambiarFoto";
            }
        } catch (Exception e) {
            log.error("Error al procesar la actualización de foto: ", e);
            flash.addFlashAttribute("error", "Error interno al procesar la foto");
            return "redirect:/cambiarFoto";
        }
    }
}
