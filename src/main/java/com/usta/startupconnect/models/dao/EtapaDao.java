package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.EtapaEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EtapaDao extends CrudRepository<EtapaEntity, Long> {

    @Transactional
    @Query("SELECT e FROM EtapaEntity e WHERE e.convocatoria.idConvocatoria = ?1 ORDER BY e.ordenEtapa")
    public List<EtapaEntity> findByConvocatoriaId(Long idConvocatoria);
    
    @Transactional
    @Query("SELECT e FROM EtapaEntity e WHERE e.estadoEtapa = ?1")
    public List<EtapaEntity> findByEstado(boolean estado);
    
    @Transactional
    @Query("SELECT e FROM EtapaEntity e WHERE e.nombreEtapa LIKE %?1%")
    public List<EtapaEntity> findByNombre(String nombre);
    
    @Transactional
    @Modifying
    @Query("UPDATE EtapaEntity e SET e.estadoEtapa = ?2 WHERE e.idEtapa = ?1")
    public int actualizarEstadoEtapa(Long idEtapa, boolean estado);
    
    @Transactional
    @Modifying
    @Query("UPDATE EtapaEntity e SET e.nombreEtapa = ?2, e.descripcionEtapa = ?3, e.ordenEtapa = ?4 WHERE e.idEtapa = ?1")
    public int actualizarDatosEtapa(Long idEtapa, String nombre, String descripcion, int orden);
}
