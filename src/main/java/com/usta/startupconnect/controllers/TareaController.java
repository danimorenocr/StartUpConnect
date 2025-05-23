package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.EventoEntity;
import com.usta.startupconnect.entities.TareaEntity;
import com.usta.startupconnect.models.services.EtapaService;
import com.usta.startupconnect.models.services.TareaService;
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
public class TareaController {
    @Autowired
    private TareaService tareaService;
    @Autowired
    private EtapaService etapaService;

    @GetMapping(value = "/tarea")
    public String listaTareas(Model model) {
        model.addAttribute("title", "Tareas");
        model.addAttribute("urlRegistro", "/crearTarea");
        List<TareaEntity> lista = tareaService.findAll();
        lista.sort(Comparator.comparing(TareaEntity::getId));
        model.addAttribute("tareas", lista);
        return "/tarea/listarTareas";
    }

    @GetMapping(value = "/crearTarea")
    public String crearTarea(Model model) {
        List<EtapaEntity> etapas = etapaService.findAll();
        model.addAttribute("etapas", etapas);
        model.addAttribute("title", "Crear Tarea");
        TareaEntity tarea = new TareaEntity();
        model.addAttribute("tarea", tarea);
        return "/tarea/crearTarea";
    }

    @PostMapping("/crearTarea")
    public String crearTarea(@Valid @ModelAttribute("tarea") TareaEntity tarea,
            BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getFieldErrors());
            System.out.println("SHDFNKSDKFJSDJFSDJFKSKJDF No FUNCIONA");
            return "tarea/crearTarea";
        }

        tareaService.save(tarea);
        System.out.println("SHDFNKSDKFJSDJFSDJFKSKJDF YS FUNCIONA");
        return "redirect:/tarea";
    }

    @GetMapping(value = "/editarTarea/{id}")
    public String editarTarea(Model model, @PathVariable(value = "id") Long id) {
        TareaEntity tarea = tareaService.findById(id);
        List<EtapaEntity> etapas = etapaService.findAll();
        model.addAttribute("etapas", etapas);
        model.addAttribute("title", "Editar tarea");
        model.addAttribute("tareaEditar", tarea);
        return "tarea/editarTarea";
    }

    @PostMapping("/editarTarea/{id}")
    public String editarEvento(@Valid TareaEntity tarea,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "tarea/editarTarea";
        }

        TareaEntity tareaExistente = tareaService.findById(tarea.getId());
        tarea.setFechaEntrega(tareaExistente.getFechaEntrega());
        tareaService.save(tarea);
        redirectAttributes.addFlashAttribute("mensajeExito", "Tarea actualizado exitosamente");
        return "redirect:/tarea";
    }

    @RequestMapping("/eliminarTarea/{id}")
    public String eliminarTarea(@PathVariable("id") Long idTarea) {
        tareaService.deleteById(idTarea);
        return "redirect:/tarea";
    }

    @GetMapping(value = "/verTarea/{id}")
    public String verTarea(Model model, @PathVariable(value = "id") Long id) {
        TareaEntity tarea = tareaService.findById(id);
        model.addAttribute("title", "Ver tarea");
        model.addAttribute("tareaDetalle", tarea);
        return "tarea/detalleTarea";
    }
}
