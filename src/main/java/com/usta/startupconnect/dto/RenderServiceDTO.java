package com.usta.startupconnect.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenderServiceDTO {
    private String id;
    private String name;
    private String type;
    private String suspended;
    private String ownerId;
    private String repo;
    private String branch;
    private String createdAt;
    private String updatedAt;
    private String url;
    private String region;
    private String plan;
    private RenderServiceStatus status;
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RenderServiceStatus {
        private String status;
        private String updatedAt;
    }
}
