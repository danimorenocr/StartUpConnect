package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.EmprendedorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface StartupDao extends CrudRepository<StartupEntity, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT s FROM StartupEntity s WHERE s.emprendedor = ?1")
    List<StartupEntity> findByEmprendedor(EmprendedorEntity emprendedor);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM StartupEntity s WHERE s.sector = ?1")
    List<StartupEntity> findBySector(String sector);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM StartupEntity s WHERE s.estado = ?1")
    List<StartupEntity> findByEstado(String estado);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM StartupEntity s WHERE s.fechaCreacion BETWEEN ?1 AND ?2")
    List<StartupEntity> findByFechaCreacionBetween(Date fechaInicio, Date fechaFin);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM StartupEntity s WHERE s.nombreStartup LIKE %?1%")
    List<StartupEntity> findByNombreStartupContaining(String nombreStartup);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM StartupEntity s WHERE s.descripcion LIKE %?1%")
    List<StartupEntity> findByDescripcionContaining(String descripcion);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM StartupEntity s WHERE s.cantLikes >= ?1")
    List<StartupEntity> findByCantLikesGreaterThanEqual(Integer cantLikes);
}
