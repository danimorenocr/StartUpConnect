package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.MentorEntity;

public interface EtapaService {
    public List<EtapaEntity> findAll();
    
    public void save(EtapaEntity etapa);

    public EtapaEntity findById(Long id);

    public void deleteById(Long id);
    
    public List<EtapaEntity> findByMentor(MentorEntity mentor);
}
