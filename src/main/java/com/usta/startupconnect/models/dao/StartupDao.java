package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.StartupEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StartupDao extends CrudRepository<StartupEntity, Long> {

    @Transactional
    @Query("SELECT s FROM StartupEntity s")
    public List<StartupEntity> findAllStartups();
    
    @Transactional
    @Query("SELECT s FROM StartupEntity s WHERE s.nombreStartup LIKE %?1%")
    public List<StartupEntity> findByNombre(String nombre);
    
    @Transactional
    @Query("SELECT s FROM StartupEntity s WHERE s.sectorStartup = ?1")
    public List<StartupEntity> findBySector(String sector);
    
    @Transactional
    @Query("SELECT s FROM StartupEntity s WHERE s.emprendedor.idEmprendedor = ?1")
    public List<StartupEntity> findByEmprendedorId(Long idEmprendedor);
    
    @Transactional
    @Modifying
    @Query("UPDATE StartupEntity s SET s.estadoStartup = ?2 WHERE s.idStartup = ?1")
    public int actualizarEstadoStartup(Long idStartup, boolean estado);
    
    @Transactional
    @Modifying
    @Query("UPDATE StartupEntity s SET s.nombreStartup = ?2, s.descripcionStartup = ?3, s.sectorStartup = ?4 WHERE s.idStartup = ?1")
    public int actualizarDatosStartup(Long idStartup, String nombre, String descripcion, String sector);
}
