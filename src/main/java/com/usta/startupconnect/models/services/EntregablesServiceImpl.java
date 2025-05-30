package com.usta.startupconnect.models.services;
// com.usta.startupconnect.models.services.EntregablesServiceImpl

import com.usta.startupconnect.entities.EntregableEntity;
import com.usta.startupconnect.models.dao.EntregablesDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntregablesServiceImpl implements EntregablesService {

    @Autowired
    private EntregablesDao entregablesRepository;

    @Override
    public List<EntregableEntity> findAll() {
        return entregablesRepository.findAll();
    }

    @Override
    public EntregableEntity findById(Long id) {
        Optional<EntregableEntity> optional = entregablesRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public EntregableEntity save(EntregableEntity entregable) {
        return entregablesRepository.save(entregable);
    }

    @Override
    public void deleteById(Long id) {
        entregablesRepository.deleteById(id);
    }
    
    @Override
    public List<EntregableEntity> findByIdTarea(Long idTarea) {
        return entregablesRepository.findByIdTarea(idTarea);
    }

    @Override
    public List<EntregableEntity> searchByNombreArchivoOrTareaTitulo(String search) {
        return entregablesRepository.searchByNombreArchivoOrTareaTitulo(search);
    }
}
