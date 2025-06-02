package com.usta.startupconnect.controllers;

import com.usta.startupconnect.dto.RenderServiceDTO;
import com.usta.startupconnect.models.services.RenderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequestMapping("/render")
public class RenderController {

    private final RenderService renderService;

    public RenderController(RenderService renderService) {
        this.renderService = renderService;
    }

    @GetMapping("/api/services")
    @ResponseBody
    public Mono<List<RenderServiceDTO>> getAllRenderServices() {
        return renderService.listServices();
    }
      @GetMapping("/api/services/{id}")
    @ResponseBody
    public Mono<ResponseEntity<RenderServiceDTO>> getServiceById(@PathVariable String id) {
        return renderService.getServiceById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/api/services/{id}/redeploy")
    @ResponseBody
    public Mono<ResponseEntity<RenderServiceDTO>> redeployService(@PathVariable String id) {
        return renderService.redeployService(id)
                .map(service -> ResponseEntity.ok().body(service))
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new RenderServiceDTO()));
                });
    }
    
    @GetMapping("/servicios")
    public String renderPage() {
        return "render/servicios"; 
    }
}
