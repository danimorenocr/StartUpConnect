package com.usta.startupconnect.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "FEEDBACKS")
@Data
public class FeedbackEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comentario_startup")
    @Size(min = 20, max = 200)
    private String comentarioStartup;

    @Column(name = "comentario_mentor")
    @Size(min = 20, max = 200)
    private String comentarioMentor;

    @Column(name = "calificacion_startup")
    private Short calificacionStartup;

    @Column(name = "calificacion_mentor")
    private Short calificacionMentor;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private Date fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_startup")
    private StartupEntity startup;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_mentor")
    private MentorEntity mentor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_etapa")
    private EtapaEntity etapa;
}