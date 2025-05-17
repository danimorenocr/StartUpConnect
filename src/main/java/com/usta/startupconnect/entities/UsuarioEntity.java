package com.usta.startupconnect.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "USUARIOS")

public class UsuarioEntity {

    @Id
    @Column(name = "documento")
    private String documento;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombreUsu;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String emailUsu;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "contrasenna", length = 100, nullable = false)
    private String contrasenna;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fecha_creacion;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "telefono", length = 20, nullable = false)
    private String telefono;

    @NotNull
    @Size(min = 20, max = 200)
    @Column(name = "foto_url", length = 200, nullable = false)
    private String fotoUrl;

    @NotNull
    @JoinColumn(name = "id_rol", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RolEntity rol;

    public String getDocumento() {
        return documento;
    }

    public String getNombreUsu() {
        return nombreUsu;
    }

    public String getEmailUsu() {
        return emailUsu;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public LocalDate getFecha_creacion() {
        return fecha_creacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public RolEntity getRol() {
        return rol;
    }
}
