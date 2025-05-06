package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.EntregableEntity;
import com.usta.startupconnect.entities.TareaEntity;

public interface EntregablesService {
    public List<EntregableEntity> findAll();
    
    public void save(EntregableEntity entregable);
    
    public EntregableEntity findById(Long id);
    
    public void deleteById(Long id);
    
    public EntregableEntity actualizar(EntregableEntity entregable);
    
    public List<EntregableEntity> findByNombreArchivoContaining(String nombreArchivo);
    
    public List<EntregableEntity> findByTarea(TareaEntity tarea);
    
    public List<EntregableEntity> findByEstado(String estado);
    
    public Long countByTarea(TareaEntity tarea);
    
    public Long countByEstado(String estado);
    
    public EntregableEntity actualizarEstado(Long id, String nuevoEstado);
    
    public List<EntregableEntity> findByTareaAndEstado(TareaEntity tarea, String estado);

}
