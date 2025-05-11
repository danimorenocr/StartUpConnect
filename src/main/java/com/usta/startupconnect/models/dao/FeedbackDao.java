package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.FeedbackEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.MentorEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface FeedbackDao extends CrudRepository<FeedbackEntity, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT f FROM FeedbackEntity f WHERE f.startup = ?1")
    List<FeedbackEntity> findByStartup(StartupEntity startup);

    @Transactional(readOnly = true)
    @Query("SELECT f FROM FeedbackEntity f WHERE f.mentor = ?1")
    List<FeedbackEntity> findByMentor(MentorEntity mentor);

    @Transactional(readOnly = true)
    @Query("SELECT f FROM FeedbackEntity f WHERE f.fechaCreacion BETWEEN ?1 AND ?2")
    List<FeedbackEntity> findByFechaCreacionBetween(Date fechaInicio, Date fechaFin);

    @Transactional(readOnly = true)
    @Query("SELECT f FROM FeedbackEntity f WHERE f.calificacionStartup >= ?1")
    List<FeedbackEntity> findByCalificacionStartupGreaterThanEqual(Short calificacion);

    @Transactional(readOnly = true)
    @Query("SELECT f FROM FeedbackEntity f WHERE f.calificacionMentor >= ?1")
    List<FeedbackEntity> findByCalificacionMentorGreaterThanEqual(Short calificacion);

}
