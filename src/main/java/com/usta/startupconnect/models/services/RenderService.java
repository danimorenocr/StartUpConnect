package com.usta.startupconnect.models.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.startupconnect.dto.RenderEnvVarDTO;
import com.usta.startupconnect.dto.RenderServiceCreateDTO;
import com.usta.startupconnect.dto.RenderServiceDTO;
import com.usta.startupconnect.dto.RenderServiceListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class RenderService {
    private static final Logger logger = LoggerFactory.getLogger(RenderService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${render.api.key}")
    private String apiKey;

    private final WebClient renderWebClient;

    public RenderService(WebClient renderWebClient) {
        this.renderWebClient = renderWebClient;
    }    public Mono<RenderServiceListDTO> listServices() {
        // Verificar si la API key está configurada
        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.error("La API key de Render no está configurada");
            return Mono.error(new RuntimeException("API key de Render no configurada"));
        }
          // Ocultar la API key en los logs
        String apiKeyPreview = maskSensitiveData(apiKey);
        logger.info("Consultando servicios de Render con API key: {}", apiKeyPreview);
        
        return renderWebClient.get()
                .uri("/services")
                .header("Authorization", "Bearer " + apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(this::processResponse)
                .onErrorResume(WebClientResponseException.class, e -> {
                    int statusCode = e.getStatusCode().value();
                    
                    // Personalizar mensajes de error según el código de estado HTTP
                    String errorMessage;
                    if (statusCode == 401 || statusCode == 403) {
                        errorMessage = "Error de autenticación: API key inválida o expirada";
                        logger.error("Error de autenticación en API de Render: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                    } else if (statusCode == 404) {
                        errorMessage = "El recurso solicitado no existe en Render";
                        logger.error("Recurso no encontrado en API de Render: {}", e.getResponseBodyAsString());
                    } else if (statusCode >= 500) {
                        errorMessage = "Error en el servidor de Render, intente más tarde";
                        logger.error("Error del servidor de Render: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                    } else {
                        errorMessage = "Error en la API de Render: " + e.getStatusCode();
                        logger.error("Error en la API de Render: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                    }
                    
                    return Mono.error(new RuntimeException(errorMessage));
                })
                .onErrorResume(e -> {
                    logger.error("Error al procesar servicios de Render", e);
                    return Mono.error(new RuntimeException("Error al procesar servicios de Render: " + e.getMessage()));
                });
    }private Mono<RenderServiceListDTO> processResponse(String jsonResponse) {
        try {
            // Imprimir la respuesta JSON para depuración (limitada a los primeros 500 caracteres)
            String logJsonPreview = jsonResponse;
            if (jsonResponse != null && jsonResponse.length() > 500) {
                logJsonPreview = jsonResponse.substring(0, 500) + "... (truncado)";
            }
            logger.info("Respuesta de Render (preview): {}", logJsonPreview);
            
            // Si la respuesta está vacía, devolver una lista vacía
            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                logger.warn("La respuesta de Render está vacía");
                return Mono.just(new RenderServiceListDTO(Collections.emptyList()));
            }
            
            // Intentar deserializar como array de servicios
            try {
                RenderServiceDTO[] services = objectMapper.readValue(jsonResponse, RenderServiceDTO[].class);
                logger.info("Deserializado correctamente como array con {} elementos", services.length);
                return Mono.just(new RenderServiceListDTO(Arrays.asList(services)));
            } catch (JsonProcessingException arrayError) {
                logger.debug("No es un array, intentando como objeto", arrayError);
                
                // Intentar deserializar como objeto que contiene services
                try {
                    RenderServiceListDTO serviceList = objectMapper.readValue(jsonResponse, RenderServiceListDTO.class);
                    int count = serviceList.getServices() != null ? serviceList.getServices().size() : 0;
                    logger.info("Deserializado correctamente como objeto con {} servicios", count);
                    return Mono.just(serviceList);
                } catch (JsonProcessingException objectError) {
                    logger.error("Error al deserializar como objeto con lista de servicios", objectError);
                    
                    // Último intento: deserializar como un solo objeto RenderServiceDTO
                    try {
                        RenderServiceDTO singleService = objectMapper.readValue(jsonResponse, RenderServiceDTO.class);
                        logger.info("Deserializado correctamente como un único servicio");
                        return Mono.just(new RenderServiceListDTO(Collections.singletonList(singleService)));
                    } catch (JsonProcessingException singleError) {
                        logger.error("Error al deserializar como servicio único", singleError);
                        return Mono.error(new RuntimeException("Error al procesar la respuesta de Render: formato no reconocido"));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error general al procesar la respuesta", e);
            return Mono.error(new RuntimeException("Error al procesar la respuesta de Render: " + e.getMessage()));
        }
    }

    public Mono<String> getRawServiceJson() {
        return renderWebClient.get()
                .uri("/services")
                .header("Authorization", "Bearer " + apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    logger.error("Error al obtener JSON de Render: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                    return Mono.error(new RuntimeException("Error al obtener datos de Render: " + e.getStatusCode() + " " + e.getMessage()));
                });
    }    public Mono<RenderServiceDTO> createService(RenderServiceCreateDTO createDTO) {
        // Verificar si la API key está configurada
        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.error("La API key de Render no está configurada");
            return Mono.error(new RuntimeException("API key de Render no configurada"));
        }
        
        try {
            // Verificar y manejar valores por defecto
            if (createDTO.getBranch() == null || createDTO.getBranch().trim().isEmpty()) {
                createDTO.setBranch("main");
            }
            
            if (createDTO.getAutoDeploy() == null) {
                createDTO.setAutoDeploy(true);
            }
            
            // Valores por defecto para plan y región si no se proporcionan
            if (createDTO.getPlan() == null || createDTO.getPlan().trim().isEmpty()) {
                createDTO.setPlan("free");
            }
            
            if (createDTO.getRegion() == null || createDTO.getRegion().trim().isEmpty()) {
                createDTO.setRegion("oregon");
            }
            
            // Limpiar variables de entorno vacías
            if (createDTO.getEnvironmentVariables() != null) {
                List<RenderEnvVarDTO> cleanedEnvVars = createDTO.getEnvironmentVariables().stream()
                    .filter(envVar -> envVar.getKey() != null && !envVar.getKey().trim().isEmpty())
                    .toList();
                createDTO.setEnvironmentVariables(cleanedEnvVars);
            }
            
            // Convertir el DTO a JSON para enviarlo a la API
            String requestBody = objectMapper.writeValueAsString(createDTO);
            logger.info("Enviando solicitud para crear servicio de Render: {}", createDTO.getName());
            logger.debug("Cuerpo de la solicitud: {}", requestBody);
            
            return renderWebClient.post()
                    .uri("/services")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMap(jsonResponse -> {
                        logger.info("Servicio creado exitosamente: {}", createDTO.getName());
                        try {
                            return Mono.just(objectMapper.readValue(jsonResponse, RenderServiceDTO.class));
                        } catch (JsonProcessingException e) {
                            logger.error("Error al deserializar la respuesta de creación de servicio", e);
                            return Mono.error(new RuntimeException("Error al procesar la respuesta de Render: " + e.getMessage()));
                        }
                    })
                    .onErrorResume(WebClientResponseException.class, e -> {
                        int statusCode = e.getStatusCode().value();
                        String responseBody = e.getResponseBodyAsString();
                        
                        // Personalizar mensajes de error según el código de estado HTTP
                        String errorMessage;
                        if (statusCode == 401 || statusCode == 403) {
                            errorMessage = "Error de autenticación: API key inválida o expirada";
                            logger.error("Error de autenticación en API de Render: {} - {}", e.getStatusCode(), responseBody);
                        } else if (statusCode == 400) {
                            errorMessage = "Error en la solicitud: " + responseBody;
                            logger.error("Error en la solicitud a API de Render: {}", responseBody);
                        } else if (statusCode == 404) {
                            errorMessage = "El recurso solicitado no existe en Render";
                            logger.error("Recurso no encontrado en API de Render: {}", responseBody);
                        } else if (statusCode >= 500) {
                            errorMessage = "Error en el servidor de Render, intente más tarde";
                            logger.error("Error del servidor de Render: {} - {}", e.getStatusCode(), responseBody);
                        } else {
                            errorMessage = "Error en la API de Render: " + e.getStatusCode();
                            logger.error("Error en la API de Render: {} - {}", e.getStatusCode(), responseBody);
                        }
                        
                        return Mono.error(new RuntimeException(errorMessage));
                    })
                    .onErrorResume(e -> {
                        if (e instanceof WebClientResponseException) {
                            // Ya manejado arriba
                            return Mono.error(e);
                        }
                        logger.error("Error al crear servicio en Render", e);
                        return Mono.error(new RuntimeException("Error al crear servicio en Render: " + e.getMessage()));
                    });
        } catch (JsonProcessingException e) {
            logger.error("Error al serializar la solicitud de creación de servicio", e);
            return Mono.error(new RuntimeException("Error al preparar la solicitud: " + e.getMessage()));
        }
    }

    // Método de utilidad para ofuscar información sensible en los logs
    private String maskSensitiveData(String data) {
        if (data == null || data.length() <= 8) {
            return "***";
        }
        // Mostrar solo los primeros 4 y últimos 4 caracteres
        return data.substring(0, 4) + "..." + data.substring(data.length() - 4);
    }
}
