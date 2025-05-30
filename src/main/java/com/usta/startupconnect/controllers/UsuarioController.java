package com.usta.startupconnect.controllers;

import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.RolEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.ConvocatoriaService;
import com.usta.startupconnect.models.services.GoogleMeetService;
import com.usta.startupconnect.models.services.MentorService;
import com.usta.startupconnect.models.services.RolService;
import com.usta.startupconnect.models.services.StartupService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RolService rolService;
    @Autowired
    private GoogleMeetService googleMeetService;
    @Autowired
    private StartupService startupService;
    @Autowired
    private MentorService mentorService;
    @Autowired
    private ConvocatoriaService convocatoriaService;    @GetMapping(value = "/administrador")
    public String administrador(Model model, org.springframework.security.core.Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getName();
            UsuarioEntity usuario = usuarioService.findByEmail(email);
            if (usuario != null) {
                model.addAttribute("nombreCompleto", usuario.getNombreUsu());
                model.addAttribute("userEmail", email);
            } else {
                model.addAttribute("nombreCompleto", "Usuario");
            }
        } else {
            model.addAttribute("nombreCompleto", "Usuario");
        }
        
        // Obtener datos para el dashboard
        
        // 1. Total de usuarios
        List<UsuarioEntity> usuarios = usuarioService.findAll();
        model.addAttribute("totalUsers", usuarios.size());
        
        // 2. Startups - Total y distribución por sector
        List<StartupEntity> startups = startupService.findAll();
        model.addAttribute("totalStartups", startups.size());
        
        // Conteo de startups por sector
        Map<String, Long> startupsPorSector = startups.stream()
                .collect(Collectors.groupingBy(
                        startup -> startup.getSector() != null ? startup.getSector() : "Otros", 
                        Collectors.counting()));
        model.addAttribute("startupsPorSector", startupsPorSector);
        
        // 3. Mentores - Total y distribución por especialidad
        List<MentorEntity> mentores = mentorService.findAll();
        model.addAttribute("totalMentors", mentores.size());
        
        // Conteo de mentores por especialidad
        Map<String, Long> mentoresPorEspecialidad = mentores.stream()
                .collect(Collectors.groupingBy(
                        mentor -> mentor.getEspecialidad() != null ? mentor.getEspecialidad() : "Otros", 
                        Collectors.counting()));
        model.addAttribute("mentoresPorEspecialidad", mentoresPorEspecialidad);
        
        // 4. Convocatorias - Total y por estado
        List<ConvocatoriaEntity> convocatorias = convocatoriaService.findAll();
        model.addAttribute("totalConvocatorias", convocatorias.size());
        
        // Clasificar convocatorias por estado (activas, próximas a cerrar, en revisión)
        Date hoy = new Date();
        Date proximosSieteDias = sumarDiasAFecha(hoy, 7);
        
        long convocatoriasActivas = convocatorias.stream()
                .filter(c -> c.getFechaInicio().before(hoy) && c.getFechaFin().after(hoy))
                .count();
        
        long convocatoriasProximasACerrar = convocatorias.stream()
                .filter(c -> c.getFechaInicio().before(hoy) && c.getFechaFin().after(hoy) && c.getFechaFin().before(proximosSieteDias))
                .count();
        
        long convocatoriasEnRevision = convocatorias.stream()
                .filter(c -> c.getFechaFin().before(hoy))
                .count();
        
        model.addAttribute("convocatoriasActivas", convocatoriasActivas);
        model.addAttribute("convocatoriasProximasACerrar", convocatoriasProximasACerrar);
        model.addAttribute("convocatoriasEnRevision", convocatoriasEnRevision);
        
        // Obtener próxima convocatoria por cerrar
        try {
            Optional<ConvocatoriaEntity> proximaConvocatoria = convocatorias.stream()
                    .filter(c -> c.getFechaFin().after(hoy))
                    .min(Comparator.comparing(ConvocatoriaEntity::getFechaFin));
            
            if (proximaConvocatoria.isPresent()) {
                ConvocatoriaEntity convocatoria = proximaConvocatoria.get();
                // Verificar que todos los campos necesarios estén presentes
                if (convocatoria.getTitulo() != null && convocatoria.getFechaFin() != null) {
                    model.addAttribute("proximaConvocatoria", convocatoria);
                }
            }
        } catch (Exception e) {
            logger.error("Error al obtener próxima convocatoria: {}", e.getMessage());
            // No agregamos la convocatoria al modelo si hay error
        }
        
        // Obtener eventos próximos y configuración del calendario
        try {
            // Obtener eventos próximos
            List<Event> upcomingEvents = googleMeetService.listUpcomingEvents(5);
            model.addAttribute("upcomingEvents", upcomingEvents);
            
            // Obtener URL del calendario con autenticación
            String calendarEmbedUrl = googleMeetService.getCalendarEmbedUrl();
            model.addAttribute("calendarEmbedUrl", calendarEmbedUrl);
            
            // Obtener lista de calendarios disponibles (opcional, para futuras mejoras)
            List<CalendarListEntry> calendars = googleMeetService.getCalendars();
            model.addAttribute("calendars", calendars);
            
        } catch (Exception e) {
            logger.error("Error al cargar eventos de Google Calendar: {}", e.getMessage(), e);
            model.addAttribute("calendarError", "No se pudieron cargar los eventos: " + e.getMessage());
            // Agregar valores por defecto para que la plantilla no falle
            model.addAttribute("upcomingEvents", Collections.emptyList());
            model.addAttribute("calendarEmbedUrl", "about:blank");
            model.addAttribute("calendars", Collections.emptyList());
        }
        
        return "/administrador/dashboardAdmin";
    }
    
    // Método auxiliar para sumar días a una fecha
    private Date sumarDiasAFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
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

@GetMapping(value = "/registro")
public String registroUsuario(Model model) {
    List<RolEntity> roles = rolService.findAll();
    model.addAttribute("roles", roles);
    model.addAttribute("title", "Registro de usuario");
    model.addAttribute("usuario", new UsuarioEntity());
    return "registro"; 
}

@PostMapping(value = "/registro")
public String guardarRegistroUsuario(@Valid UsuarioEntity usuario, @RequestParam(value = "foto", required = false) MultipartFile foto,
        BindingResult result, RedirectAttributes redirectAttributes) {
    String urlImagen = guardarImagen(foto);
    if (result.hasErrors()) {
        System.out.println(result.getAllErrors());
        return "registro";
    }

    usuario.setFotoUrl(urlImagen);
    usuario.setFecha_creacion(LocalDate.now());
    usuarioService.save(usuario);

    redirectAttributes.addFlashAttribute("mensajeExito", "Usuario registrado exitosamente");
    return "redirect:/login";
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
    public String eliminarHabitacion(@PathVariable(value = "id") String id, RedirectAttributes redirectAttributes) {
        System.out.println("Intentando eliminar habitación con ID: " + id);

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

        return "redirect:/usuario";
    }

    @GetMapping(value = "/editarUsuario/{id}")
    public String editarUsuario(Model model, @PathVariable(value = "id") String id) {
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

        String documento =usuario.getDocumento();
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
    public String verUsuario(Model model, @PathVariable(value = "id") String id) {
        UsuarioEntity usuario = usuarioService.findById(id);
        List<RolEntity> roles = rolService.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("title", "Editar Usuario");
        model.addAttribute("usuarioEditar", usuario);
        model.addAttribute("imagen", usuario.getFotoUrl());
        return "usuario/detalleUsuario";
    }

}
