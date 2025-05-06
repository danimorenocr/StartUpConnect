package com.usta.startupconnect.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "ENTREGABLES")
@Data

public class EntregableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre_archivo", length = 100, nullable = false)
    private String nombreArchivo;

    @Column(name = "ruta_archivo", columnDefinition = "TEXT")
    private String rutaArchivo;

    @Size(max = 50)
    @Column(name = "estado", length = 50)
    private String estado;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tarea", referencedColumnName = "id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TareaEntity tarea;

}
