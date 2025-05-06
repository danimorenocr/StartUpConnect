package com.usta.startupconnect.entities;

import jakarta.persistence.*;
import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "EVENTOS")
@Data
public class EventoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 5, max = 100)
    @Column(name = "titulo")
    private String titulo;

    @NotNull
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private Date fecha;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "color")
    private String color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_convocatoria")
    private ConvocatoriaEntity convocatoria;
}