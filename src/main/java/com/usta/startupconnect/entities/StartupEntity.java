package com.usta.startupconnect.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "STARTUPS")

public class StartupEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombreStartup;

    @Size(min = 1, max = 50)
    @Column(name = "sector", length = 50)
    private String sector;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "pagina_web", columnDefinition = "TEXT")
    private String paginaWeb;

    @Column(name = "logo_url", columnDefinition = "TEXT")
    private String logoUrl;

    @Column(name = "pitch_url", columnDefinition = "TEXT")
    private String pitchUrl;

    @Column(name = "video_presentacion", columnDefinition = "TEXT")
    private String videoPresentacion;

    @Column(name = "github", columnDefinition = "TEXT")
    private String github;

    @Column(name = "linkedin", columnDefinition = "TEXT")
    private String linkedin;

    @Column(name = "instagram", columnDefinition = "TEXT")
    private String instagram;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Size(min = 1, max = 50)
    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "cant_likes")
    private Integer cantLikes = 0;

    @NotNull
    @JoinColumn(name = "id_emprendedor", referencedColumnName = "documento")
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private EmprendedorEntity emprendedor;
}
