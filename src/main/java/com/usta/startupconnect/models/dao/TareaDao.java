package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.TareaEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface TareaDao extends CrudRepository<TareaEntity, Long> {

    @Transactional
    @Query("SELECT t FROM TareaEntity t WHERE t.etapa.idEtapa = ?1")
    public List<TareaEntity> findByEtapaId(Long idEtapa);
    
    @Transactional
    @Query("SELECT t FROM TareaEntity t WHERE t.tituloTarea LIKE %?1%")
    public List<TareaEntity> findByTitulo(String titulo);
    
    @Transactional
    @Query("SELECT t FROM TareaEntity t WHERE t.fechaLimiteTarea <= ?1 AND t.estadoTarea = false")
    public List<TareaEntity> findTareasPendientesPorVencer(Date fecha);
    
    @Transactional
    @Modifying
    @Query("UPDATE TareaEntity t SET t.estadoTarea = ?2 WHERE t.idTarea = ?1")
    public int actualizarEstadoTarea(Long idTarea, boolean estado);
    
    @Transactional
    @Modifying
    @Query("UPDATE TareaEntity t SET t.descripcionTarea = ?2, t.fechaLimiteTarea = ?3, t.prioridadTarea = ?4 WHERE t.idTarea = ?1")
    public int actualizarDatosTarea(Long idTarea, String descripcion, Date fechaLimite, int prioridad);
}
