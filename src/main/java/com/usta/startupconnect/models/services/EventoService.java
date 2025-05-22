package com.usta.startupconnect.models.services;

import java.util.List;
import com.usta.startupconnect.entities.EventoEntity;

public interface EventoService {

    public List<EventoEntity> findAll();

    public void save(EventoEntity usuario);

    public EventoEntity findById(Long id);

    public void deleteById(Long id);

}
