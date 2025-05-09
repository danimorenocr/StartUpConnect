package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.FeedbackEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.models.dao.FeedbackDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Service
public class FeedbackServiceImplement implements FeedbackService {
    @Autowired
    private FeedbackDao feedbackDAO;

    @Override
    @Transactional
    public List<FeedbackEntity> findAll() {
        return (List<FeedbackEntity>) feedbackDAO.findAll();
    }

    @Override
    @Transactional
    public void save(FeedbackEntity feedback) {
        feedbackDAO.save(feedback);
    }

    @Override
    @Transactional
    public FeedbackEntity findById(Long id) {
        return feedbackDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        feedbackDAO.deleteById(id);
    }

    @Override
    @Transactional
    public FeedbackEntity actualizar(FeedbackEntity feedback) {
        return feedbackDAO.save(feedback);
    }
    
    @Override
    @Transactional
    public List<FeedbackEntity> findByStartup(StartupEntity startup) {
        return feedbackDAO.findByStartupId(startup.getId());
    }
    
    @Override
    @Transactional
    public List<FeedbackEntity> findByMentor(MentorEntity mentor) {
        return feedbackDAO.findByMentorId(Long.valueOf(mentor.getDocumento()));
    }
    
    @Override
    @Transactional
    public List<FeedbackEntity> findByFechaCreacionBetween(Date fechaInicio, Date fechaFin) {
        return feedbackDAO.findByFechaBetween(fechaInicio, fechaFin);
    }
    
    @Override
    @Transactional
    public List<FeedbackEntity> findByCalificacionStartupGreaterThanEqual(Short calificacion) {
        return feedbackDAO.findByCalificacionMinima(calificacion);
    }
    
    @Override
    @Transactional
    public List<FeedbackEntity> findByCalificacionMentorGreaterThanEqual(Short calificacion) {
        return new ArrayList<>();
    }
    
    @Override
    @Transactional
    public Double promedioCalificacionByStartup(StartupEntity startup) {
        List<FeedbackEntity> feedbacks = findByStartup(startup);
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        
        double suma = 0.0;
        int count = 0;
        for (FeedbackEntity feedback : feedbacks) {
            if (feedback.getCalificacionStartup() != null) {
                suma += feedback.getCalificacionStartup();
                count++;
            }
        }
        
        return count > 0 ? suma / count : 0.0;
    }
    
    @Override
    @Transactional
    public Double promedioCalificacionByMentor(MentorEntity mentor) {
        List<FeedbackEntity> feedbacks = findByMentor(mentor);
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        
        double suma = 0.0;
        int count = 0;
        for (FeedbackEntity feedback : feedbacks) {
            if (feedback.getCalificacionMentor() != null) {
                suma += feedback.getCalificacionMentor();
                count++;
            }
        }
        
        return count > 0 ? suma / count : 0.0;
    }
}