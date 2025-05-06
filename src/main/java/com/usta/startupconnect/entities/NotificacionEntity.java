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

    @NotNull
    @Size(min = 5, max = 50)
    @Column(name = "tipo")
    private String tipo;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private Date fecha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_calendario")
    private CalendarioEntity calendario;
}