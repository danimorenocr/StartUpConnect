package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.TareaEntity;
import com.usta.startupconnect.entities.EtapaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface TareaDao extends CrudRepository<TareaEntity, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT t FROM TareaEntity t WHERE t.etapa = ?1")
    List<TareaEntity> findByEtapa(EtapaEntity etapa);

    @Transactional(readOnly = true)
    @Query("SELECT t FROM TareaEntity t WHERE t.fechaEntrega BETWEEN ?1 AND ?2")
    List<TareaEntity> findByFechaEntregaBetween(Date fechaInicio, Date fechaFin);

    @Transactional(readOnly = true)
    @Query("SELECT t FROM TareaEntity t WHERE t.titulo LIKE %?1%")
    List<TareaEntity> findByTituloContaining(String titulo);

    @Transactional(readOnly = true)
    @Query("SELECT t FROM TareaEntity t WHERE t.descripcion LIKE %?1%")
    List<TareaEntity> findByDescripcionContaining(String descripcion);

    @Transactional(readOnly = true)
    @Query("SELECT t FROM TareaEntity t WHERE t.fechaEntrega >= ?1")
    List<TareaEntity> findByFechaEntregaGreaterThanEqual(Date fecha);

}
