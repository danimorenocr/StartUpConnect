package com.usta.startupconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingInfo {
    private String eventId;
    private String fecha;
    private String hora;
    private Integer duracion;
    private String enlace;
}
