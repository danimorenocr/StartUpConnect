package com.usta.startupconnect.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificacionDTO {
    private String mensaje;
    private String tipoEntidad; // Ej: "tarea", "evento", "convocatoria"
    private Long entidadId;
    private Date fecha;
    private Boolean leido;
    private String idUsuario;

}