package com.usta.startupconnect.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "POSTULACIONES")

public class PostulacionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre_proyecto", length = 100, nullable = false)
    private String nombreProyecto;

    @NotNull
    @Column(name = "problema_a_solucionar", columnDefinition = "TEXT")
    private String problemaASolucionar;

    @NotNull
    @Column(name = "solucion", columnDefinition = "TEXT")
    private String solucion;

    @NotNull
    @Column(name = "clientes_objetivo", columnDefinition = "TEXT")
    private String clientesObjetivo;

    @NotNull
    @Column(name = "diferenciador", columnDefinition = "TEXT")
    private String diferenciador;

    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl;

    @Column(name = "enlace_pagina_web", columnDefinition = "TEXT")
    private String enlacePaginaWeb;

    @NotNull
    @Column(name = "numero_integrantes")
    private Short numeroIntegrantes;

    @NotNull
    @Column(name = "roles_integrantes", columnDefinition = "TEXT")
    private String rolesIntegrantes;

    @NotNull
    @Column(name = "dinero_ventas", precision = 10, scale = 2)
    private BigDecimal dineroVentas;

    @NotNull
    @Column(name = "habilidades_equipo", columnDefinition = "TEXT")
    private String habilidadesEquipo;

    @NotNull
    @Size(max = 50)
    @Column(name = "etapa_proyecto", length = 50)
    private String etapaProyecto;

    @NotNull
    @Column(name = "necesidades_actuales", columnDefinition = "TEXT")
    private String necesidadesActuales;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_postulacion")
    private Date fechaPostulacion;

    @NotNull
    @Size(max = 50)
    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "pitch", columnDefinition = "TEXT")
    private String pitch;

    @Column(name = "documento_propuesta", columnDefinition = "TEXT")
    private String documentoPropuesta;

    @Column(name = "video_pitch", columnDefinition = "TEXT")
    private String videoPitch;

    @Column(name = "enlaces_adicionales", columnDefinition = "TEXT")
    private String enlacesAdicionales;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_startup", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StartupEntity startup;

    @OneToOne
    @JoinColumn(name = "idConvocatoria", unique = true)
    private ConvocatoriaEntity convocatoria;
}
