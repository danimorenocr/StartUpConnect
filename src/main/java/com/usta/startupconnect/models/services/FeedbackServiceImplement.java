package com.usta.startupconnect.models.services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usta.startupconnect.entities.FeedbackEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.models.dao.FeedbackDao;

@Service
public class FeedbackServiceImplement implements FeedbackService {
    @Autowired
    private FeedbackDao feedbackDao;

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackEntity> findAll() {
        return (List<FeedbackEntity>) feedbackDao.findAll();
    }

    @Override
    @Transactional
    public void save(FeedbackEntity feedback) {
        feedbackDao.save(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackEntity findById(Long id) {
        return feedbackDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        feedbackDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackEntity> findByMentor(MentorEntity mentor) {
        return feedbackDao.findByMentor(mentor);
    }

    @Override
    @Transactional(readOnly = true)
    public MentorEntity findMentorByEtapa(EtapaEntity etapa) {
        return feedbackDao.findMentorByEtapa(etapa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EtapaEntity> findEtapasByMentor(MentorEntity mentor) {
        return feedbackDao.findEtapasByMentor(mentor);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<FeedbackEntity> findByStartup(StartupEntity startup) {
        return feedbackDao.findByStartup(startup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StartupEntity> findStartupsByEtapa(EtapaEntity etapa) {
        return feedbackDao.findStartupsByEtapa(etapa);
    }
}