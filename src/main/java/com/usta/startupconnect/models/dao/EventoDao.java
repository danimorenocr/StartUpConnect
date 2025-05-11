package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.EventoEntity;
import com.usta.startupconnect.entities.ConvocatoriaEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface EventoDao extends CrudRepository<EventoEntity, Long> {

    @Transactional
    @Query("SELECT e FROM EventoEntity e")
    public List<EventoEntity> findAllEventos();


    @Transactional(readOnly = true)
    @Query("SELECT e FROM EventoEntity e WHERE e.convocatoria = ?1")
    List<EventoEntity> findByConvocatoria(ConvocatoriaEntity convocatoria);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM EventoEntity e WHERE e.fecha = ?1")
    List<EventoEntity> findByFecha(Date fecha);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM EventoEntity e WHERE e.titulo LIKE %?1%")
    List<EventoEntity> findByTituloContaining(String titulo);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM EventoEntity e WHERE e.color = ?1")
    List<EventoEntity> findByColor(String color);
}
