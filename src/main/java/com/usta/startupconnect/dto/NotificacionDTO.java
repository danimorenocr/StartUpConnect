package com.usta.startupconnect.dto;

import lombok.Data;

@Data
public class NotificacionDTO {
    private String mensaje;
    private String tipoEntidad;  // Ej: "tarea", "evento", "convocatoria"
    private Long entidadId;
    private String idUsuario;    // Porque el ID de UsuarioEntity es String
}