package com.usta.startupconnect.models.services;

import java.util.Date;
import java.util.List;

import com.usta.startupconnect.entities.FeedbackEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.entities.StartupEntity;

public interface FeedbackService {
    public List<FeedbackEntity> findAll();

    public void save(FeedbackEntity feedback);

    public FeedbackEntity findById(Long id);

    public void deleteById(Long id);

    public FeedbackEntity actualizar(FeedbackEntity feedback);

    public List<FeedbackEntity> findByStartup(StartupEntity startup);

    public List<FeedbackEntity> findByMentor(MentorEntity mentor);

    public List<FeedbackEntity> findByFechaCreacionBetween(Date fechaInicio, Date fechaFin);

    public List<FeedbackEntity> findByCalificacionStartupGreaterThanEqual(Short calificacion);

    public List<FeedbackEntity> findByCalificacionMentorGreaterThanEqual(Short calificacion);

    public Double promedioCalificacionByStartup(StartupEntity startup);

    public Double promedioCalificacionByMentor(MentorEntity mentor);

}
