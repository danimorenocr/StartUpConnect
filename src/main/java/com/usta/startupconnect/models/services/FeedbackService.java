package com.usta.startupconnect.models.services;

import java.util.Collection;
import java.util.List;

import com.usta.startupconnect.entities.FeedbackEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.EtapaEntity;

public interface FeedbackService {
    public List<FeedbackEntity> findAll();
    
    public MentorEntity findMentorByEtapa(EtapaEntity etapa);
    
    public List<EtapaEntity> findEtapasByMentor(MentorEntity mentor);
    
    public List<StartupEntity> findStartupsByEtapa(EtapaEntity etapa);

    public void save(FeedbackEntity feedback);    public List<FeedbackEntity> findByMentor(MentorEntity mentor);

    public FeedbackEntity findById(Long id);

    public void deleteById(Long id);

    public Collection<FeedbackEntity> findByStartup(StartupEntity startup);
}
