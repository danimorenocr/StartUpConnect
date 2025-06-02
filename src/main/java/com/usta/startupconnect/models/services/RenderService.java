package com.usta.startupconnect.models.services;

import com.usta.startupconnect.dto.RenderServiceDTO;
import com.usta.startupconnect.dto.RenderServiceWrapperDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class RenderService {

    private final WebClient renderWebClient;
    private final String apiKey;

    public RenderService(WebClient renderWebClient, @Value("${render.api.key}") String apiKey) {
        this.renderWebClient = renderWebClient;
        this.apiKey = apiKey;
    }

    public Mono<List<RenderServiceDTO>> listServices() {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return Mono.error(new RuntimeException("API key no configurada"));
        }

        return renderWebClient.get()
                .uri("/services")
                .header("Authorization", "Bearer " + apiKey)
                .retrieve()
                .bodyToFlux(RenderServiceWrapperDTO.class) // deserializa cada objeto
                .map(RenderServiceWrapperDTO::getService) // saca solo el `service`
                .collectList(); // junta en una lista
    }
      public Mono<RenderServiceDTO> getServiceById(String id) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return Mono.error(new RuntimeException("API key no configurada"));
        }
        
        if (id == null || id.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El ID del servicio no puede estar vacío"));
        }

        return renderWebClient.get()
                .uri("/services/" + id)
                .header("Authorization", "Bearer " + apiKey)
                .retrieve()
                .bodyToMono(RenderServiceDTO.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode().is4xxClientError()) {
                        return Mono.empty(); // Retornar vacío si no se encuentra
                    }
                    return Mono.error(e); // Propagar otros errores
                });
    }
    
    public Mono<RenderServiceDTO> redeployService(String id) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return Mono.error(new RuntimeException("API key no configurada"));
        }
        
        if (id == null || id.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El ID del servicio no puede estar vacío"));
        }
        
        return renderWebClient.post()
                .uri("/services/" + id + "/deploys")
                .header("Authorization", "Bearer " + apiKey)
                .retrieve()
                .bodyToMono(RenderServiceDTO.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    return Mono.error(new RuntimeException("Error al redeploy: " + e.getMessage(), e));
                });
    }
}
