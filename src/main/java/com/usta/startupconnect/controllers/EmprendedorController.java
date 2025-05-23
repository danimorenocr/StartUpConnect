package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.services.EmprendedorService;
import com.usta.startupconnect.models.services.StartupService;
import com.usta.startupconnect.models.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.util.Comparator;
import java.util.List;

@Controller
public class EmprendedorController {

    @Autowired
    private EmprendedorService emprendedorService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private StartupService startupService;

    @GetMapping("/vitrina")
    public String mostrarVitrina(Model model) {
        List<EmprendedorEntity> emprendedores = emprendedorService.findAll();
        List<StartupEntity> startups = startupService.findAll();
        
        model.addAttribute("emprendedores", emprendedores);
        model.addAttribute("startups", startups);
        
        return "vitrina";
    }

    @GetMapping(value = "/emprendedor")
    public String listarEmprendedores(Model model) {
        model.addAttribute("title", "Emprendedores");
        model.addAttribute("crearEmprendedor", "/crearEmprendedor");
        List<EmprendedorEntity> lista = emprendedorService.findAll();
        lista.sort(Comparator.comparing(EmprendedorEntity::getDocumento));
        model.addAttribute("emprendedores", lista);
        return "/emprendedor/listarEmprendedores";
    }

    @GetMapping(value = "/crearEmprendedor")
    public String crearEmprendedor(Model model) {
        model.addAttribute("title", "Crear emprendedor");
        model.addAttribute("emprendedor", new EmprendedorEntity());
        return "/emprendedor/formCrearEmprendedor";
    }

    @PostMapping(value = "/crearEmprendedor")
    public String guardarEmprendedor(@Valid EmprendedorEntity emprendedor, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "emprendedor/formCrearEmprendedor";
        }

        String documentoUsuario = emprendedor.getDocumento();
        UsuarioEntity usuario = usuarioService.findById(documentoUsuario);

        emprendedor.setUsuario(usuario);

        emprendedorService.save(emprendedor);
        redirectAttributes.addFlashAttribute("mensajeExito", "Emprendedor Saved Successfully");
        return "redirect:/emprendedor";
    }

    @PostMapping(value = "/eliminarEmprendedor/{id}")
    public String eliminarEmprendedor(@PathVariable(value = "id") String id, RedirectAttributes redirectAttributes) {
        try {

            EmprendedorEntity emprendedor = emprendedorService.findById(id);

            if (emprendedor == null) {
                redirectAttributes.addFlashAttribute("error", "Emprendedor no encontrado");
                return "redirect:/emprendedor";
            }

            emprendedorService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Emprendedor eliminado correctamente");
            return "redirect:/emprendedor";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el emprendedor: " + e.getMessage());
            return "redirect:/emprendedor";
        }
    }

    @GetMapping(value = "/verEmprendedor/{id}")
    public String verEmprendedor(Model model, @PathVariable(value = "id") String id) {
        EmprendedorEntity emprendedor = emprendedorService.findById(id);
        model.addAttribute("title", "Ver Emprendedor");
        model.addAttribute("emprendedorDetalle", emprendedor);
        return "emprendedor/detalleEmprendedor";
    }


    @GetMapping(value = "/editarEmprendedor/{id}")
    public String editarUsuario(Model model, @PathVariable(value = "id") String id) {
        EmprendedorEntity emprendedor = emprendedorService.findById(id);
        model.addAttribute("title", "Editar Emprendedor");
        model.addAttribute("emprendedorEditar", emprendedor);
        return "emprendedor/editarEmprendedor";
    }

    @PostMapping("/editarEmprendedor/{id}")
    public String actualizarEmprendedor(@Valid EmprendedorEntity emprendedor,
            BindingResult result,
            @PathVariable("id") String id,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "emprendedor/editarEmprendedor";
        }

        EmprendedorEntity emprendedorExistente = emprendedorService.findById(id);
        if (emprendedorExistente == null) {
            redirectAttributes.addFlashAttribute("error", "Emprendedor no encontrado");
            return "redirect:/emprendedor";
        }

        emprendedor.setUsuario(emprendedorExistente.getUsuario());

        emprendedorService.save(emprendedor);

        redirectAttributes.addFlashAttribute("mensajeExito", "Emprendedor actualizado exitosamente");
        return "redirect:/emprendedor";
    }

}
