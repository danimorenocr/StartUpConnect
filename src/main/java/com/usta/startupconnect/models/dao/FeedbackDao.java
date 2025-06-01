package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.FeedbackEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.EtapaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeedbackDao extends CrudRepository<FeedbackEntity, Long> {
    public List<FeedbackEntity> findByMentor(MentorEntity mentor);

    @Query("SELECT DISTINCT f.mentor FROM FeedbackEntity f WHERE f.etapa = ?1")
    MentorEntity findMentorByEtapa(EtapaEntity etapa);

    @Query("SELECT DISTINCT f.etapa FROM FeedbackEntity f WHERE f.mentor = ?1")
    List<EtapaEntity> findEtapasByMentor(MentorEntity mentor);

    @Query("SELECT DISTINCT f FROM FeedbackEntity f WHERE f.startup = ?1")
    List<FeedbackEntity> findByStartup(StartupEntity startup);

    @Query("SELECT DISTINCT f.startup FROM FeedbackEntity f WHERE f.etapa = ?1")
    List<StartupEntity> findStartupsByEtapa(EtapaEntity etapa);
}
