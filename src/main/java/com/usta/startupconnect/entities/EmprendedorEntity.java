package com.usta.startupconnect.entities;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "EMPRENDEDORES")

public class EmprendedorEntity {

    @Id
    @Column(name = "documento")
    private String documento;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "universidad", length = 100, nullable = false)
    private String universidad;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "programa_educativo", length = 100, nullable = false)
    private String programaEducativo;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario", referencedColumnName = "documento")
    private UsuarioEntity usuario;

}
