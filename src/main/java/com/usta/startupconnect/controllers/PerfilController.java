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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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

            // Actualizar datos básicos del usuario
            usuarioActual.setNombreUsu(usuario.getNombreUsu());
            usuarioActual.setEmailUsu(usuario.getEmailUsu());
            usuarioActual.setTelefono(usuario.getTelefono());

            // Guardar cambios del usuario
            usuarioService.save(usuarioActual);

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
            RedirectAttributes flash) {
        try {
            // Verificar que el usuario exista
            UsuarioEntity usuario = usuarioService.findById(documento);
            if (usuario == null) {
                flash.addFlashAttribute("error", "No se encontró el usuario");
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

            // Verificar la longitud mínima de la contraseña
            if (passwordNueva.length() < 6) {
                flash.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres");
                return "redirect:/cambiarPassword";
            }

            // Actualizar la contraseña
            usuario.setContrasenna(passwordEncoder.encode(passwordNueva));
            usuarioService.save(usuario);

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

            // Crear directorio base si no existe
            Path uploadDir = Paths.get("src", "main", "resources", "static", "uploads", "usuarios").toAbsolutePath();
            Files.createDirectories(uploadDir);

            // Eliminar foto anterior si existe
            if (usuario.getFotoUrl() != null && !usuario.getFotoUrl().isEmpty()) {
                try {
                    String oldPath = usuario.getFotoUrl().replace("/", File.separator);
                    Path fullPath = Paths.get(uploadDir.getParent().getParent().toString(), "static", oldPath);
                    Files.deleteIfExists(fullPath);
                    log.info("Foto anterior eliminada: " + fullPath);
                } catch (IOException e) {
                    log.warn("No se pudo eliminar la foto anterior: " + e.getMessage());
                }
            }

            // Generar nombre único para la foto
            String originalFilename = foto.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // Construir la ruta completa
            Path destinationFile = uploadDir.resolve(uniqueFilename);

            // Guardar el archivo usando REPLACE_EXISTING para evitar conflictos
            Files.copy(foto.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            log.info("Foto guardada en: " + destinationFile);

            // Actualizar ruta de la foto en la BD (usar forward slashes para URLs)
            usuario.setFotoUrl("/uploads/usuarios/" + uniqueFilename);
            usuarioService.save(usuario);

            flash.addFlashAttribute("success", "Foto de perfil actualizada correctamente");
            return "redirect:/perfil";
        } catch (IOException e) {
            log.error("Error al subir la foto: ", e);
            flash.addFlashAttribute("error", "Error al subir la foto: No se pudo guardar el archivo");
            return "redirect:/cambiarFoto";
        } catch (Exception e) {
            log.error("Error al actualizar la foto: ", e);
            flash.addFlashAttribute("error", "Error al actualizar la foto: Error interno del servidor");
            return "redirect:/cambiarFoto";
        }
    }
}
