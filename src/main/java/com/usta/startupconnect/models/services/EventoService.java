package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.EventoEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.StartupEntity;

public interface EventoService {

    public List<EventoEntity> findAll();

    public void save(EventoEntity evento);

    public EventoEntity findById(Long id);

    public void deleteById(Long id);

    public EventoEntity actualizar(EventoEntity evento);

    public List<EventoEntity> findByStartup(StartupEntity startup);

    public List<EventoEntity> findByEmprendedor(EmprendedorEntity emprendedor);

    public List<EventoEntity> findByMentor(MentorEntity mentor);
}
