package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.EventoEntity;
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
    
    @Transactional
    @Query("SELECT e FROM EventoEntity e WHERE e.fechaEvento >= ?1")
    public List<EventoEntity> findEventosFuturos(Date fechaActual);
    
    @Transactional
    @Query("SELECT e FROM EventoEntity e WHERE e.fechaEvento BETWEEN ?1 AND ?2")
    public List<EventoEntity> findByFechaBetween(Date fechaInicio, Date fechaFin);
    
    @Transactional
    @Query("SELECT e FROM EventoEntity e WHERE e.tituloEvento LIKE %?1%")
    public List<EventoEntity> findByTitulo(String titulo);
    
    @Transactional
    @Modifying
    @Query("UPDATE EventoEntity e SET e.estadoEvento = ?2 WHERE e.idEvento = ?1")
    public int actualizarEstadoEvento(Long idEvento, boolean estado);
    
    @Transactional
    @Modifying
    @Query("UPDATE EventoEntity e SET e.tituloEvento = ?2, e.descripcionEvento = ?3, e.fechaEvento = ?4, e.lugarEvento = ?5 WHERE e.idEvento = ?1")
    public int actualizarDatosEvento(Long idEvento, String titulo, String descripcion, Date fecha, String lugar);
}
