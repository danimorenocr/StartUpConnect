package com.usta.startupconnect.entities;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "MENTORES")

public class MentorEntity {

    @Id
    @Column(name = "documento")
    private String documento;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "especialidad", length = 100, nullable = false)
    private String especialidad;

    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "biografia", length = 250, nullable = false)
    private String biografia;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "linkedin", length = 200, nullable = false)
    private String linkedin;

    @NotNull
    @Column(name = "anos_experiencia", nullable = false)
    private Short anosExperiencia;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idUsuario", referencedColumnName = "documento", insertable = false, updatable = false)
    private UsuarioEntity usuario;
}
