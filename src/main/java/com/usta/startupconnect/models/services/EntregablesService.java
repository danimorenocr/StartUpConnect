package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.EntregableEntity;

public interface EntregablesService {
    List<EntregableEntity> findAll();

    EntregableEntity findById(Long id);

    EntregableEntity save(EntregableEntity entregable);

    void deleteById(Long id);
    
    // Método para buscar entregables por ID de tarea
    List<EntregableEntity> findByIdTarea(Long idTarea);
    
    // Buscar entregables por nombre de archivo o título de tarea
    List<EntregableEntity> searchByNombreArchivoOrTareaTitulo(String search);
}
