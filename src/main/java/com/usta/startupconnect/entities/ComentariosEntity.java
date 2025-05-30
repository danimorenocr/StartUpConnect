package com.usta.startupconnect.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "COMENTARIOS")
public class ComentariosEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Long idComentario;

    @Column(name = "comentario", nullable = false, length = 500)
    private String comentario;
    
    @ManyToOne
    @JoinColumn(name = "id_startup", nullable = false)
    private StartupEntity startup;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "documento", nullable = false)
    private UsuarioEntity usuario;

}