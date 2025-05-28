package com.usta.startupconnect.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RenderServiceCreateDTO {
    private String name;
    private String type;
    private String repo;
    private String branch;
    
    @JsonProperty("autoDeploy")
    private Boolean autoDeploy;
    
    private String plan;
    private String region;
    
    @JsonProperty("envVars")
    private List<RenderEnvVarDTO> environmentVariables = new ArrayList<>();
    
    // Para servicios web
    private String buildCommand;
    private String startCommand;
    
    // Para sitios estáticos
    private String publishDirectory;
    
    // Constructor vacío para deserialización
    public RenderServiceCreateDTO() {
    }
    
    // Constructor completo
    public RenderServiceCreateDTO(String name, String type, String repo, String branch, 
                                Boolean autoDeploy, String plan, String region, 
                                List<RenderEnvVarDTO> environmentVariables,
                                String buildCommand, String startCommand, String publishDirectory) {
        this.name = name;
        this.type = type;
        this.repo = repo;
        this.branch = branch;
        this.autoDeploy = autoDeploy;
        this.plan = plan;
        this.region = region;
        this.environmentVariables = environmentVariables;
        this.buildCommand = buildCommand;
        this.startCommand = startCommand;
        this.publishDirectory = publishDirectory;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Boolean getAutoDeploy() {
        return autoDeploy;
    }

    public void setAutoDeploy(Boolean autoDeploy) {
        this.autoDeploy = autoDeploy;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<RenderEnvVarDTO> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(List<RenderEnvVarDTO> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public String getBuildCommand() {
        return buildCommand;
    }

    public void setBuildCommand(String buildCommand) {
        this.buildCommand = buildCommand;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public void setStartCommand(String startCommand) {
        this.startCommand = startCommand;
    }

    public String getPublishDirectory() {
        return publishDirectory;
    }

    public void setPublishDirectory(String publishDirectory) {
        this.publishDirectory = publishDirectory;
    }
}
