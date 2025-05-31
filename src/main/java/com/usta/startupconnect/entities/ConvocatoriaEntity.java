package com.usta.startupconnect.entities;

import jakarta.persistence.*;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CONVOCATORIAS")
@Data
public class ConvocatoriaEntity implements Serializable {

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
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_fin")
    private Date fechaFin;

    @NotNull
    @Column(name = "requisitos", columnDefinition = "TEXT")
    private String requisitos;

    @NotNull
    @Size(min = 20, max = 100)
    @Column(name = "organizador")
    private String organizador;

    @NotNull
    @Column(name = "beneficios", columnDefinition = "TEXT")
    private String beneficios;

    @NotNull
    @Size(min = 5, max = 50)
    @Column(name = "contacto")
    private String contacto;

    @NotNull
    @Size(min = 20, max = 100)
    @Column(name = "sector_objetivo")
    private String sectorObjetivo;    @OneToMany(mappedBy = "convocatoria", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostulacionEntity> postulaciones;


}