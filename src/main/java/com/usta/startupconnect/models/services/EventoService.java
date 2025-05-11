package com.usta.startupconnect.models.services;

import java.util.List;
import java.util.Date;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.entities.EventoEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.ConvocatoriaEntity;

public interface EventoService {

    public List<EventoEntity> findAll();

}
