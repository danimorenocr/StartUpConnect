package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.EventoEntity;
import com.usta.startupconnect.models.services.ConvocatoriaService;
import com.usta.startupconnect.models.services.EventoService;
import com.usta.startupconnect.models.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

@Controller
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @Autowired
    private ConvocatoriaService convocatoriaService;

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping(value = "/evento")
    public String listarEventos(Model model) {
        model.addAttribute("title", "Eventos");
        model.addAttribute("urlRegistro", "/crearEvento");
        List<EventoEntity> lista = eventoService.findAll();
        lista.sort(Comparator.comparing(EventoEntity::getId));
        model.addAttribute("eventos", lista);
        return "/evento/listarEventos";
    }

    @GetMapping(value = "/crearEvento")
    public String crearEvento(Model model) {
        List<ConvocatoriaEntity> convocatorias = convocatoriaService.findAll();
        model.addAttribute("convocatorias", convocatorias);
        model.addAttribute("title", "Crear Evento");
        EventoEntity evento = new EventoEntity();
        model.addAttribute("evento", evento);
        return "/evento/crearEvento";
    }

    @PostMapping("/crearEvento")
    public String crearEvento(@Valid @ModelAttribute("evento") EventoEntity evento,
            BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getFieldErrors());
            System.out.println("SHDFNKSDKFJSDJFSDJFKSKJDF No FUNCIONA");
            return "evento/crearEvento";
        }

        eventoService.save(evento);
        // Notificar a mentores y emprendedores sobre el nuevo evento
        String mensaje = "📅 Nuevo evento creado: " + evento.getTitulo();
        notificacionService.notificarUsuariosPorRol("MENTOR", mensaje);
        notificacionService.notificarUsuariosPorRol("EMPRENDEDOR", mensaje);

        System.out.println("SHDFNKSDKFJSDJFSDJFKSKJDF YS FUNCIONA");
        return "redirect:/evento";
    }

    @GetMapping(value = "/editarEvento/{id}")
    public String editarEvento(Model model, @PathVariable(value = "id") Long id) {
        EventoEntity evento = eventoService.findById(id);
        List<ConvocatoriaEntity> convocatorias = convocatoriaService.findAll();
        model.addAttribute("convocatorias", convocatorias);
        model.addAttribute("title", "Editar evento");
        model.addAttribute("eventoEditar", evento);
        return "evento/editarEvento";
    }

    @PostMapping("/editarEvento/{id}")
    public String editarEvento(@Valid EventoEntity evento,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "evento/editarEvento";
        }

        EventoEntity eventoExistente = eventoService.findById(evento.getId());
        evento.setFecha(eventoExistente.getFecha());
        eventoService.save(evento);
        redirectAttributes.addFlashAttribute("mensajeExito", "Evento actualizado exitosamente");
        return "redirect:/evento";
    }

    @RequestMapping("/eliminarEvento/{id}")
    public String eliminarEvento(@PathVariable("id") Long idEvento) {
        eventoService.deleteById(idEvento);
        return "redirect:/evento";
    }

    @GetMapping(value = "/verEvento/{id}")
    public String verEvento(Model model, @PathVariable(value = "id") Long id) {
        EventoEntity evento = eventoService.findById(id);
        model.addAttribute("title", "Ver evento");
        model.addAttribute("eventoDetalle", evento);
        return "evento/detalleEvento";
    }

}
