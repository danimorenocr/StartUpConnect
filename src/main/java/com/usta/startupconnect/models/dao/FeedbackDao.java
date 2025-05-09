package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.FeedbackEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface FeedbackDao extends CrudRepository<FeedbackEntity, Long> {

    @Transactional
    @Query("SELECT f FROM FeedbackEntity f WHERE f.mentor.idMentor = ?1")
    public List<FeedbackEntity> findByMentorId(Long idMentor);
    
    @Transactional
    @Query("SELECT f FROM FeedbackEntity f WHERE f.startup.idStartup = ?1")
    public List<FeedbackEntity> findByStartupId(Long idStartup);
    
    @Transactional
    @Query("SELECT f FROM FeedbackEntity f WHERE f.fechaFeedback BETWEEN ?1 AND ?2")
    public List<FeedbackEntity> findByFechaBetween(Date fechaInicio, Date fechaFin);
    
    @Transactional
    @Query("SELECT f FROM FeedbackEntity f WHERE f.calificacionFeedback >= ?1")
    public List<FeedbackEntity> findByCalificacionMinima(int calificacionMinima);
    
    @Transactional
    @Modifying
    @Query("UPDATE FeedbackEntity f SET f.comentarioFeedback = ?2 WHERE f.idFeedback = ?1")
    public int actualizarComentario(Long idFeedback, String comentario);
    
    @Transactional
    @Modifying
    @Query("UPDATE FeedbackEntity f SET f.calificacionFeedback = ?2 WHERE f.idFeedback = ?1")
    public int actualizarCalificacion(Long idFeedback, int calificacion);
}
