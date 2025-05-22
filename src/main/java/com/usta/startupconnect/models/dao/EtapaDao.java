package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.MentorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface EtapaDao extends CrudRepository<EtapaEntity, Long> {


    @Transactional(readOnly = true)
    @Query("SELECT e FROM EtapaEntity e WHERE e.mentor = ?1")
    List<EtapaEntity> findByMentor(MentorEntity mentor);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM EtapaEntity e WHERE e.titulo LIKE %?1%")
    List<EtapaEntity> findByTituloContaining(String titulo);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM EtapaEntity e WHERE e.fechaInicio BETWEEN ?1 AND ?2")
    List<EtapaEntity> findByFechaInicioBetween(Date fechaInicio, Date fechaFin);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM EtapaEntity e WHERE e.fechaFin BETWEEN ?1 AND ?2")
    List<EtapaEntity> findByFechaFinBetween(Date fechaInicio, Date fechaFin);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM EtapaEntity e WHERE e.fechaInicio <= ?1 AND e.fechaFin >= ?1")
    List<EtapaEntity> findByFechaActual(Date fecha);

    @Transactional(readOnly = true)
    @Query("SELECT COUNT(e) FROM EtapaEntity e WHERE e.mentor = ?1")
    Long countByMentor(MentorEntity mentor);
}
