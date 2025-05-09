package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.EntregableEntity;
import com.usta.startupconnect.entities.TareaEntity;
import com.usta.startupconnect.models.dao.EntregablesDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntregablesServiceImplement implements EntregablesService {
    @Autowired
    private EntregablesDao entregablesDAO;

    @Override
    @Transactional
    public List<EntregableEntity> findAll() {
        return (List<EntregableEntity>) entregablesDAO.findAll();
    }

    @Override
    @Transactional
    public void save(EntregableEntity entregable) {
        entregablesDAO.save(entregable);
    }

    @Override
    @Transactional
    public EntregableEntity findById(Long id) {
        return entregablesDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        entregablesDAO.deleteById(id);
    }

    @Override
    @Transactional
    public EntregableEntity actualizar(EntregableEntity entregable) {
        return entregablesDAO.save(entregable);
    }
    
    @Override
    @Transactional
    public List<EntregableEntity> findByNombreArchivoContaining(String nombreArchivo) {
        // Este método no está disponible en el DAO
        // Implementación temporal: buscar en todos los entregables
        List<EntregableEntity> todos = findAll();
        return todos.stream()
                .filter(e -> e.getNombreArchivo() != null && e.getNombreArchivo().contains(nombreArchivo))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public List<EntregableEntity> findByTarea(TareaEntity tarea) {
        return entregablesDAO.findByTareaId(tarea.getId());
    }
    
    @Override
    @Transactional
    public List<EntregableEntity> findByEstado(String estado) {
        // Este método no está disponible en el DAO
        // Implementación temporal: filtrar sobre todos los entregables
        List<EntregableEntity> todos = findAll();
        return todos.stream()
                .filter(e -> e.getEstado() != null && e.getEstado().equals(estado))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public Long countByTarea(TareaEntity tarea) {
        // Este método no está disponible en el DAO
        // Implementación temporal: contar la lista filtrada
        return (long) findByTarea(tarea).size();
    }
    
    @Override
    @Transactional
    public Long countByEstado(String estado) {
        // Este método no está disponible en el DAO
        // Implementación temporal: contar la lista filtrada
        return (long) findByEstado(estado).size();
    }
    
    @Override
    @Transactional
    public EntregableEntity actualizarEstado(Long id, String nuevoEstado) {
        EntregableEntity entregable = findById(id);
        if (entregable != null) {
            entregable.setEstado(nuevoEstado);
            return entregablesDAO.save(entregable);
        }
        return null;
    }
    
    @Override
    @Transactional
    public List<EntregableEntity> findByTareaAndEstado(TareaEntity tarea, String estado) {
        // Este método no está disponible en el DAO
        // Implementación temporal: filtrar sobre los entregables de la tarea
        List<EntregableEntity> entregablesTarea = findByTarea(tarea);
        return entregablesTarea.stream()
                .filter(e -> e.getEstado() != null && e.getEstado().equals(estado))
                .collect(Collectors.toList());
    }
}