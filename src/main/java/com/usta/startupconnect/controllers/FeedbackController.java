package com.usta.startupconnect.controllers;

import com.usta.startupconnect.entities.FeedbackEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.dao.FeedbackDao;
import com.usta.startupconnect.models.services.FeedbackService;
import com.usta.startupconnect.models.services.MentorService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
    
    @Autowired
    private FeedbackDao feedbackDao;
    
    @Autowired
    private StartupService startupService;
    
    @Autowired
    private MentorService mentorService;
    
    @Autowired
    private JpaUserDetailsService userDetailsService;
    
    @GetMapping("/feedbackStartup/{id}")
    public String mostrarFormularioFeedback(@PathVariable("id") Long idStartup, Model model, RedirectAttributes redirectAttributes) {        // Obtener la startup
        StartupEntity startup = startupService.findById(idStartup);
        if (startup == null) {
            redirectAttributes.addFlashAttribute("error", "La startup no existe");
            return "redirect:/misStartupsAsignadas";
        }
        
        // Obtener el mentor (usuario actual)
        UsuarioEntity usuario = userDetailsService.obtenerUsuarioAutenticado();
        if (usuario == null) {
            return "redirect:/login";
        }
        
        MentorEntity mentor = mentorService.findById(usuario.getDocumento());
        if (mentor == null) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos de mentor");
            return "redirect:/misStartups";
        }
        
        // Buscar si ya existe un feedback para esta startup y este mentor
        List<FeedbackEntity> feedbacksExistentes = feedbackDao.findByMentor(mentor);
        FeedbackEntity feedbackExistente = null;
        
        for (FeedbackEntity feedback : feedbacksExistentes) {
            if (feedback.getStartup().getId().equals(idStartup)) {
                feedbackExistente = feedback;
                break;
            }
        }
        
        // Si no existe, crear uno nuevo
        if (feedbackExistente == null) {
            feedbackExistente = new FeedbackEntity();
            feedbackExistente.setMentor(mentor);
            feedbackExistente.setStartup(startup);
            feedbackExistente.setFechaCreacion(new Date());
        }
        
        model.addAttribute("feedback", feedbackExistente);
        model.addAttribute("startup", startup);
        model.addAttribute("mentor", mentor);
        
        return "feedback/crearFeedback";
    }
    
    @PostMapping("/guardarFeedback")
    public String guardarFeedback(@Valid @ModelAttribute("feedback") FeedbackEntity feedback, 
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("startup", feedback.getStartup());
            model.addAttribute("mentor", feedback.getMentor());
            return "feedback/crearFeedback";
        }
        
        // Actualizar fecha si es nuevo o actualizar
        feedback.setFechaCreacion(new Date());
          // Guardar el feedback
        feedbackService.save(feedback);
        
        redirectAttributes.addFlashAttribute("mensajeExito", "Feedback guardado correctamente");
        return "redirect:/misStartupsAsignadas";
    }
}
