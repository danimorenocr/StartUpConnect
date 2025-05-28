package com.usta.startupconnect.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RenderServiceListDTO {
    private List<RenderServiceDTO> services = new ArrayList<>();

    // Constructor sin argumentos para deserialización
    public RenderServiceListDTO() {
    }

    // Constructor con lista de servicios
    public RenderServiceListDTO(List<RenderServiceDTO> services) {
        this.services = services;
    }

    public List<RenderServiceDTO> getServices() {
        return services;
    }

    public void setServices(List<RenderServiceDTO> services) {
        this.services = services;
    }
}
