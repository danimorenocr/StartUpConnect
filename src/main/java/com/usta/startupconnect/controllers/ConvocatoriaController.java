package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.models.services.ConvocatoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

@Controller
public class ConvocatoriaController {
    @Autowired
    ConvocatoriaService convocatoriaService;

    @GetMapping(value = "/convocatoria")
    public String listarConvocatorias(Model model) {
        model.addAttribute("title", "Convocatorias");
        model.addAttribute("urlRegistro", "/crearConvocatoria");
        List<ConvocatoriaEntity> lista = convocatoriaService.findAll();
        lista.sort(Comparator.comparing(ConvocatoriaEntity::getId));
        model.addAttribute("convocatorias", lista);
        return "/convocatoria/listarConvocatorias";
    }

    @GetMapping("/crearConvocatoria")
    public String crearConvocatoriaForm(Model model) {
        model.addAttribute("title", "Registrar Convocatoria");
        model.addAttribute("convocatoria", new ConvocatoriaEntity());
        return "convocatoria/crearConvocatoria";
    }

    @PostMapping("/crearConvocatoria")
    public String guardarConvocatoria(@Valid @ModelAttribute("convocatoria") ConvocatoriaEntity convocatoria,
            BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getFieldErrors());
            System.out.println("SHDFNKSDKFJSDJFSDJFKSKJDF No FUNCIONA");
            return "convocatoria/crearConvocatoria";
        }
        convocatoriaService.save(convocatoria);
        System.out.println("SHDFNKSDKFJSDJFSDJFKSKJDF YS FUNCIONA");
        return "redirect:/convocatoria";
    }

    @GetMapping("/editarConvocatoria/{id}")
    public String editarConvocatoria(@PathVariable("id") Long idConvocatoria, Model model) {
        ConvocatoriaEntity convocatoria = convocatoriaService.findById(idConvocatoria);
        model.addAttribute("title", "Editar Convocatoria");
        model.addAttribute("convocatoriaEditar", convocatoria);
        return "convocatoria/editarConvocatoria";
    }

    @PostMapping("/editarConvocatoria/{id}")
    public String actualizarArtista(@PathVariable("id") Long idConvocatoria,
            @Valid @ModelAttribute("convocatoria") ConvocatoriaEntity convocatoria,
            BindingResult result) {
        if (result.hasErrors()) {
            return "convocatoria/editarConvocatoria";
        }
        convocatoria.setId(idConvocatoria);
        convocatoriaService.save(convocatoria);
        return "redirect:/convocatoria";
    }

    @RequestMapping("/eliminarConvocatoria/{id}")
    public String eliminarConvocatoria(@PathVariable("id") Long idConvocatoria) {
        convocatoriaService.deleteById(idConvocatoria);
        return "redirect:/convocatoria";
    }

    @GetMapping(value = "/verConvocatoria/{id}")
    public String verConvocatoria(Model model, @PathVariable(value = "id") Long id) {
        ConvocatoriaEntity convocatoria = convocatoriaService.findById(id);
        model.addAttribute("title", "Ver Convocatoria");
        model.addAttribute("convocatoriaDetalle", convocatoria);
        return "convocatoria/detalleConvocatoria";

    }
}
