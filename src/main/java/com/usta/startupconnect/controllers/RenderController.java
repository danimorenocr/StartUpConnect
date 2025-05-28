package com.usta.startupconnect.controllers;

import com.usta.startupconnect.dto.RenderServiceCreateDTO;
import com.usta.startupconnect.dto.RenderServiceDTO;
import com.usta.startupconnect.dto.RenderServiceListDTO;
import com.usta.startupconnect.models.services.RenderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/render")
public class RenderController {

    private final RenderService renderService;

    public RenderController(RenderService renderService) {
        this.renderService = renderService;
    }

    @GetMapping("/services")
    public String getServicesView(Model model) {
        model.addAttribute("servicesData", renderService.listServices());
        return "render/servicios";
    }

    @GetMapping("/api/services")
    @ResponseBody
    public Mono<RenderServiceListDTO> getServicesApi() {
        return renderService.listServices();
    }

    @GetMapping("/api/services/raw")
    @ResponseBody
    public Mono<String> getRawServices() {
        return renderService.getRawServiceJson();
    }

    @GetMapping("/crear-servicio")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("title", "Crear Servicio en Render");
        model.addAttribute("serviceCreateDTO", new RenderServiceCreateDTO());
        return "render/crearServicio";
    }    @PostMapping("/crear-servicio")
    public String crearServicio(@ModelAttribute("serviceCreateDTO") RenderServiceCreateDTO serviceCreateDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Crear Servicio en Render");
            model.addAttribute("mensaje", "Hay errores en el formulario. Por favor revíselo e intente de nuevo.");
            model.addAttribute("alertClass", "alert-danger");
            return "render/crearServicio";
        }
        
        try {
            // Llamar al servicio para crear el servicio en Render
            renderService.createService(serviceCreateDTO).block(); // Usar block() en controlador para hacer síncrona la llamada
            redirectAttributes.addFlashAttribute("mensaje", "Servicio creado exitosamente");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/render/services";
        } catch (Exception e) {
            model.addAttribute("title", "Crear Servicio en Render");
            model.addAttribute("mensaje", "Error al crear el servicio: " + e.getMessage());
            model.addAttribute("alertClass", "alert-danger");
            model.addAttribute("serviceCreateDTO", serviceCreateDTO); // Mantener los datos ingresados
            return "render/crearServicio";
        }
    }

    @PostMapping("/api/services/create")
    @ResponseBody
    public Mono<RenderServiceDTO> createServiceApi(@RequestBody RenderServiceCreateDTO serviceCreateDTO) {
        return renderService.createService(serviceCreateDTO);
    }
}
