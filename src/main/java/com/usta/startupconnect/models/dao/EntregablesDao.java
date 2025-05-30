package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.EntregableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interfaz DAO para el acceso a datos de entregables
 */
@Repository
public interface EntregablesDao extends JpaRepository<EntregableEntity, Long> {
    // Método para buscar entregables por ID de tarea
    @Query("SELECT e FROM EntregableEntity e WHERE e.idTarea = ?1")
    List<EntregableEntity> findByIdTarea(Long idTarea);
}