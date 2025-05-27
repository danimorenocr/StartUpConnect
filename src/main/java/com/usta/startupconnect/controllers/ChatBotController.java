package com.usta.startupconnect.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import com.usta.startupconnect.models.services.ChatService;

@Controller
public class ChatBotController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/chat")
    @ResponseBody // This annotation indicates that the return value should be written directly to the response body
    public Mono<String> chat(@RequestParam String prompt) {
        return chatService.getChatResponse(prompt);
    }

    @GetMapping("/chatbot")
    public String mostrarChatBot(Model model) {
        model.addAttribute("title", "Chatbot de Startup Connect");
        model.addAttribute("description",
                "Bienvenido al chatbot de Startup Connect. Aquí puedes hacer preguntas y obtener respuestas sobre nuestros servicios.");
        List<String> preguntasFrecuentes = List.of(
                "¿Cómo puedo crear una cuenta?",
                "¿Qué servicios ofrece Startup Connect?",
                "¿Cómo puedo contactar al soporte?");

        model.addAttribute("preguntasFrecuentes", preguntasFrecuentes);

        return "chatbot";
    }
}