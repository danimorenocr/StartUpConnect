package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.PostulacionEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.ConvocatoriaService;
import com.usta.startupconnect.models.services.EmprendedorService;
import com.usta.startupconnect.models.services.PostulacionService;
import com.usta.startupconnect.models.services.StartupService;
import com.usta.startupconnect.security.JpaUserDetailsService;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

@Controller
public class PostulacionController {
    @Autowired
    private PostulacionService postulacionService;

    @Autowired
    private ConvocatoriaService convocatoriaService;

    @Autowired
    private StartupService startupService;

    @Autowired
    private EmprendedorService emprendedorService;

    @Autowired
    private JpaUserDetailsService userDetailsService;    @GetMapping(value = {"/postulacion", "/postulacion/convocatoria/{idConvocatoria}"})
    public String listarPostulaciones(Model model, @PathVariable(required = false) Long idConvocatoria,
            org.springframework.security.core.Authentication authentication) {
        model.addAttribute("urlRegistro", "/crearPostulacion");

        List<PostulacionEntity> listaPostulaciones;
        if (idConvocatoria != null) {
            // Si se especifica una convocatoria, obtener las postulaciones de esa convocatoria
            ConvocatoriaEntity convocatoria = convocatoriaService.findById(idConvocatoria);
            if (convocatoria != null) {
                listaPostulaciones = postulacionService.findByConvocatoria(convocatoria);
                System.out.println("🔍 DEBUG: Encontradas " + listaPostulaciones.size() + " postulaciones para la convocatoria: " + convocatoria.getTitulo());
                model.addAttribute("title", "Postulaciones - " + convocatoria.getTitulo());
                model.addAttribute("convocatoriaActual", convocatoria);
            } else {
                listaPostulaciones = new ArrayList<>();
                System.out.println("⚠️ DEBUG: Convocatoria no encontrada con ID: " + idConvocatoria);
            }
        } else {
            // Si no se especifica convocatoria, obtener todas las postulaciones
            model.addAttribute("title", "Mis Postulaciones");
            listaPostulaciones = postulacionService.findAll();
            System.out.println("🔍 DEBUG: Cargadas " + listaPostulaciones.size() + " postulaciones totales");
        }
        
        // Verificar si el usuario es administrador
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        System.out.println("👤 DEBUG: Usuario es admin: " + isAdmin);
        System.out.println("👤 DEBUG: Authorities: " + authentication.getAuthorities());
        
        List<PostulacionEntity> postulacionesFiltradas;
        
        if (isAdmin && idConvocatoria != null) {
            // Si es administrador y está viendo una convocatoria específica, mostrar todas las postulaciones
            postulacionesFiltradas = listaPostulaciones;
            System.out.println("✅ DEBUG: Admin viendo convocatoria específica - Mostrando " + postulacionesFiltradas.size() + " postulaciones");
            model.addAttribute("title", "Todas las Postulaciones - " + 
                (model.getAttribute("convocatoriaActual") != null ? 
                    ((ConvocatoriaEntity) model.getAttribute("convocatoriaActual")).getTitulo() : "Convocatoria"));
        } else {
            // Si no es administrador o no está viendo una convocatoria específica, filtrar por usuario
            postulacionesFiltradas = new ArrayList<>();
            System.out.println("🔒 DEBUG: Filtrando postulaciones por usuario...");

            // Obtener el usuario autenticado actual
            UsuarioEntity usuarioActual = userDetailsService.obtenerUsuarioAutenticado();

            if (usuarioActual != null) {
                // Buscar el emprendedor asociado al usuario actual
                EmprendedorEntity emprendedor = emprendedorService.findByDocumento(usuarioActual.getDocumento());

                if (emprendedor != null) {
                    // Obtener las startups del emprendedor actual
                    List<StartupEntity> startups = startupService.findByEmprendedor(emprendedor);

                    // Filtrar las postulaciones que pertenecen a las startups del usuario
                    for (PostulacionEntity postulacion : listaPostulaciones) {
                        if (postulacion.getStartup() != null && startups.contains(postulacion.getStartup())) {
                            postulacionesFiltradas.add(postulacion);
                        }
                    }
                }
            }
        }        postulacionesFiltradas.sort(Comparator.comparing(PostulacionEntity::getId));
        model.addAttribute("postulaciones", postulacionesFiltradas);
        model.addAttribute("isAdmin", isAdmin); // Agregar información de admin al modelo
        return "/postulacion/listarPostulaciones";
    }

    @GetMapping("/crearPostulacion")
    public String crearPostulacionForm(Model model,
            @RequestParam(name = "idConvocatoria", required = false) Long idConvocatoria) {
        model.addAttribute("title", "Registrar Postulacion");

        PostulacionEntity postulacion = new PostulacionEntity();

        // Si se proporciona un ID de convocatoria, buscamos la convocatoria y la
        // asociamos
        if (idConvocatoria != null) {
            try {
                ConvocatoriaEntity convocatoria = convocatoriaService.findById(idConvocatoria);
                if (convocatoria != null) {
                    postulacion.setConvocatoria(convocatoria);
                    model.addAttribute("convocatoriaSeleccionada", convocatoria);
                }
            } catch (Exception e) {
                // Manejo de errores si la convocatoria no existe
                System.out.println("Error al buscar la convocatoria: " + e.getMessage());
            }
        }

        // Obtener el usuario autenticado actual
        UsuarioEntity usuarioActual = userDetailsService.obtenerUsuarioAutenticado();
        List<StartupEntity> startups = new ArrayList<>();

        if (usuarioActual != null) {
            // Buscar el emprendedor asociado al usuario actual
            EmprendedorEntity emprendedor = emprendedorService.findByDocumento(usuarioActual.getDocumento());

            if (emprendedor != null) {
                // Obtener sólo las startups del emprendedor actual
                startups = startupService.findByEmprendedor(emprendedor);
            }
        }

        model.addAttribute("startups", startups);
        model.addAttribute("postulacion", postulacion);
        return "postulacion/crearPostulacion";
    }

    @PostMapping("/crearPostulacion")
    public String guardarPostulacion(@Valid @ModelAttribute("postulacion") PostulacionEntity postulacion,
            BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // En caso de error, recargamos las startups y la convocatoria para mantener la
            // vista
            UsuarioEntity usuarioActual = userDetailsService.obtenerUsuarioAutenticado();
            List<StartupEntity> startups = new ArrayList<>();

            if (usuarioActual != null) {
                EmprendedorEntity emprendedor = emprendedorService.findByDocumento(usuarioActual.getDocumento());
                if (emprendedor != null) {
                    startups = startupService.findByEmprendedor(emprendedor);
                }
            }

            model.addAttribute("startups", startups);

            // Si hay una convocatoria seleccionada, la volvemos a cargar
            if (postulacion.getConvocatoria() != null && postulacion.getConvocatoria().getId() != null) {
                ConvocatoriaEntity convocatoria = convocatoriaService.findById(postulacion.getConvocatoria().getId());
                model.addAttribute("convocatoriaSeleccionada", convocatoria);
            }

            return "postulacion/crearPostulacion";
        }

        // Establecer la fecha de postulación al día actual si no está establecida
        if (postulacion.getFechaPostulacion() == null) {
            postulacion.setFechaPostulacion(new Date());
        }

        // Establecer el estado inicial como "Pendiente" si no está establecido
        if (postulacion.getEstado() == null || postulacion.getEstado().isEmpty()) {
            postulacion.setEstado("Pendiente");
        }        // Asegurarse de que la convocatoria esté correctamente enlazada
        System.out.println("🔍 DEBUG CONVOCATORIA: postulacion.getConvocatoria() = " + postulacion.getConvocatoria());
        if (postulacion.getConvocatoria() != null) {
            System.out.println("🔍 DEBUG CONVOCATORIA ID: " + postulacion.getConvocatoria().getId());
        }
        
        if (postulacion.getConvocatoria() != null && postulacion.getConvocatoria().getId() != null) {
            ConvocatoriaEntity convocatoria = convocatoriaService.findById(postulacion.getConvocatoria().getId());
            System.out.println("✅ DEBUG: Convocatoria encontrada y asignada: " + convocatoria.getTitulo());
            postulacion.setConvocatoria(convocatoria);
        } else {
            System.out.println("⚠️ DEBUG: Convocatoria no asignada - viene como null desde el formulario");
        }

        postulacionService.save(postulacion);
        redirectAttributes.addFlashAttribute("mensajeExito", "Postulación enviada exitosamente");
        return "redirect:/postulacion";
    }

    @GetMapping("/editarPostulacion/{id}")
    public String editarPostulacion(@PathVariable("id") Long idPostulacion, Model model) {
        PostulacionEntity postulacion = postulacionService.findById(idPostulacion);

        if (postulacion == null) {
            return "redirect:/postulacion";
        }

        // Verificar que la postulación pertenece al usuario actual (por seguridad)
        UsuarioEntity usuarioActual = userDetailsService.obtenerUsuarioAutenticado();
        boolean esPermitido = false;

        if (usuarioActual != null && postulacion.getStartup() != null) {
            EmprendedorEntity emprendedor = emprendedorService.findByDocumento(usuarioActual.getDocumento());

            if (emprendedor != null) {
                List<StartupEntity> startups = startupService.findByEmprendedor(emprendedor);

                if (startups.contains(postulacion.getStartup())) {
                    esPermitido = true;
                }
            }
        }

        if (!esPermitido) {
            // Si no tiene permiso, redirigir a la lista de postulaciones
            return "redirect:/postulacion";
        }

        // Obtener las startups del usuario actual para el formulario
        List<StartupEntity> startups = new ArrayList<>();
        if (usuarioActual != null) {
            EmprendedorEntity emprendedor = emprendedorService.findByDocumento(usuarioActual.getDocumento());
            if (emprendedor != null) {
                startups = startupService.findByEmprendedor(emprendedor);
            }
        }

        model.addAttribute("startups", startups);

        // Si hay convocatoria, cargarla para mostrarla
        if (postulacion.getConvocatoria() != null) {
            model.addAttribute("convocatoriaSeleccionada", postulacion.getConvocatoria());
        }

        model.addAttribute("title", "Editar postulación");
        model.addAttribute("postulacion", postulacion);
        return "postulacion/editarPostulacion";
    }

    @PostMapping("/editarPostulacion/{id}")
    public String actualizarPostulacion(@PathVariable("id") Long idPostulacion,
            @Valid @ModelAttribute("postulacion") PostulacionEntity postulacion,
            BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        // Verificar que la postulación existe
        PostulacionEntity postulacionOriginal = postulacionService.findById(idPostulacion);
        if (postulacionOriginal == null) {
            redirectAttributes.addFlashAttribute("error", "La postulación no existe");
            return "redirect:/postulacion";
        }

        // Verificar que la postulación pertenece al usuario actual (por seguridad)
        UsuarioEntity usuarioActual = userDetailsService.obtenerUsuarioAutenticado();
        boolean esPermitido = false;

        if (usuarioActual != null && postulacionOriginal.getStartup() != null) {
            EmprendedorEntity emprendedor = emprendedorService.findByDocumento(usuarioActual.getDocumento());

            if (emprendedor != null) {
                List<StartupEntity> startups = startupService.findByEmprendedor(emprendedor);

                if (startups.contains(postulacionOriginal.getStartup())) {
                    esPermitido = true;
                }
            }
        }

        if (!esPermitido) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar esta postulación");
            return "redirect:/postulacion";
        }

        if (result.hasErrors()) {
            // En caso de error, recargamos los datos
            List<StartupEntity> startups = new ArrayList<>();

            if (usuarioActual != null) {
                EmprendedorEntity emprendedor = emprendedorService.findByDocumento(usuarioActual.getDocumento());
                if (emprendedor != null) {
                    startups = startupService.findByEmprendedor(emprendedor);
                }
            }

            model.addAttribute("startups", startups);

            // Si hay convocatoria, cargarla para mostrarla
            if (postulacionOriginal.getConvocatoria() != null) {
                model.addAttribute("convocatoriaSeleccionada", postulacionOriginal.getConvocatoria());
            }

            return "postulacion/editarPostulacion";
        }

        // Preservar datos que no deberían cambiar
        postulacion.setId(idPostulacion);
        postulacion.setFechaPostulacion(postulacionOriginal.getFechaPostulacion());

        // Asegurarse de que la convocatoria se mantenga
        postulacion.setConvocatoria(postulacionOriginal.getConvocatoria());

        postulacionService.save(postulacion);
        redirectAttributes.addFlashAttribute("mensajeExito", "Postulación actualizada exitosamente");
        return "redirect:/postulacion";
    }

    @RequestMapping("/eliminarPostulacion/{id}")
    public String eliminarPostulacion(@PathVariable("id") Long idPostulacion, RedirectAttributes redirectAttributes) {
        // Verificar que la postulación existe
        PostulacionEntity postulacion = postulacionService.findById(idPostulacion);
        if (postulacion == null) {
            redirectAttributes.addFlashAttribute("error", "La postulación no existe");
            return "redirect:/postulacion";
        }

        // Verificar que la postulación pertenece al usuario actual (por seguridad)
        UsuarioEntity usuarioActual = userDetailsService.obtenerUsuarioAutenticado();
        boolean esPermitido = false;

        if (usuarioActual != null && postulacion.getStartup() != null) {
            EmprendedorEntity emprendedor = emprendedorService.findByDocumento(usuarioActual.getDocumento());

            if (emprendedor != null) {
                List<StartupEntity> startups = startupService.findByEmprendedor(emprendedor);

                if (startups.contains(postulacion.getStartup())) {
                    esPermitido = true;
                }
            }
        }

        if (!esPermitido) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para eliminar esta postulación");
            return "redirect:/postulacion";
        }

        postulacionService.deleteById(idPostulacion);
        redirectAttributes.addFlashAttribute("mensajeExito", "Postulación eliminada exitosamente");
        return "redirect:/postulacion";
    }

    @GetMapping(value = "/verPostulacion/{id}")
    public String verPostulacion(Model model, @PathVariable(value = "id") Long id) {
        PostulacionEntity postulacion = postulacionService.findById(id);

        // Verificar que la postulación pertenece al usuario actual (por seguridad)
        UsuarioEntity usuarioActual = userDetailsService.obtenerUsuarioAutenticado();
        boolean esPermitido = false;

        if (usuarioActual != null && postulacion != null && postulacion.getStartup() != null) {
            EmprendedorEntity emprendedor = emprendedorService.findByDocumento(usuarioActual.getDocumento());

            if (emprendedor != null) {
                List<StartupEntity> startups = startupService.findByEmprendedor(emprendedor);

                if (startups.contains(postulacion.getStartup())) {
                    esPermitido = true;
                }
            }
        }

        if (!esPermitido) {
            // Si no tiene permiso, redirigir a la lista de postulaciones
            return "redirect:/postulacion";
        }

        model.addAttribute("title", "Ver postulación");
        model.addAttribute("postulacionDetalle", postulacion);
        return "postulacion/detallePostulacion";
    }
    
    @PostMapping("/cambiarEstadoPostulacion/{id}")
    public String cambiarEstadoPostulacion(@PathVariable("id") Long idPostulacion,
            @RequestParam("nuevoEstado") String nuevoEstado,
            @RequestParam(value = "comentarios", required = false) String comentarios,
            RedirectAttributes redirectAttributes,
            org.springframework.security.core.Authentication authentication) {
        
        // Verificar que el usuario es administrador
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para realizar esta acción");
            return "redirect:/postulacion";
        }
        
        // Verificar que la postulación existe
        PostulacionEntity postulacion = postulacionService.findById(idPostulacion);
        if (postulacion == null) {
            redirectAttributes.addFlashAttribute("error", "La postulación no existe");
            return "redirect:/postulacion";
        }
        
        // Validar el nuevo estado
        if (!nuevoEstado.equals("Aprobada") && !nuevoEstado.equals("Rechazada") && !nuevoEstado.equals("En revisión")) {
            redirectAttributes.addFlashAttribute("error", "Estado no válido");
            return "redirect:/postulacion";
        }
        
        // Cambiar el estado
        String estadoAnterior = postulacion.getEstado();
        postulacion.setEstado(nuevoEstado);
        
        // Si hay comentarios, agregarlos (podríamos crear un campo para esto en el futuro)
        // Por ahora los guardamos en un log del sistema
        if (comentarios != null && !comentarios.trim().isEmpty()) {
            System.out.println("📝 ADMIN COMMENT: Estado de postulación " + idPostulacion + 
                " cambiado de '" + estadoAnterior + "' a '" + nuevoEstado + "'. Comentarios: " + comentarios);
        }
        
        postulacionService.save(postulacion);
        
        redirectAttributes.addFlashAttribute("mensajeExito", 
            "Estado de la postulación cambiado exitosamente de '" + estadoAnterior + "' a '" + nuevoEstado + "'");
        
        // Redirigir de vuelta a la vista de postulaciones de la convocatoria
        if (postulacion.getConvocatoria() != null) {
            return "redirect:/postulacion/convocatoria/" + postulacion.getConvocatoria().getId();
        } else {
            return "redirect:/postulacion";
        }
    }
    
    @GetMapping("/gestionarPostulacion/{id}")
    public String gestionarPostulacion(@PathVariable("id") Long idPostulacion, Model model,
            org.springframework.security.core.Authentication authentication) {
        
        // Verificar que el usuario es administrador
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin) {
            return "redirect:/postulacion";
        }
        
        PostulacionEntity postulacion = postulacionService.findById(idPostulacion);
        if (postulacion == null) {
            return "redirect:/postulacion";
        }
        
        model.addAttribute("title", "Gestionar Postulación - " + postulacion.getNombreProyecto());
        model.addAttribute("postulacion", postulacion);
        
        return "postulacion/gestionarPostulacion";
    }
}
