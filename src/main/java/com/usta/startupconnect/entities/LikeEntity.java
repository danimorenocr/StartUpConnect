package com.usta.startupconnect.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "LIKES")
@Data
public class LikeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "startup_id", nullable = false)
    private StartupEntity startup;
    
    @ManyToOne
    @JoinColumn(name = "usuario_documento", referencedColumnName = "documento", nullable = false)
    private UsuarioEntity usuario;
}