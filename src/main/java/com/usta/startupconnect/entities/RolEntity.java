package com.usta.startupconnect.entities;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ROLES")

public class RolEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idRol;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "rol", length = 50, nullable = false)
    private String rol;

    public RolEntity(String Rol) {
        super();
        this.rol = Rol;
    }

    public RolEntity() {

    }
}
