package com.usta.startupconnect.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "NOTIFICACIONES")
@Data
public class NotificacionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "mensaje", columnDefinition = "TEXT")
    private String mensaje;

    /**
     * Ejemplo: 'tarea', 'evento', 'convocatoria'
     */
    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "tipo_entidad")
    private String tipoEntidad;

    /**
     * ID del objeto referenciado (en su tabla original)
     */
    @NotNull
    @Column(name = "entidad_id")
    private Long entidadId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "leido")
    private Boolean leido = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private UsuarioEntity usuario; // <-- Cambia esto según tu entidad de usuario real
}
