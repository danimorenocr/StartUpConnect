package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.EmprendedorEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmprendedorDao extends CrudRepository<EmprendedorEntity, Long> {

    @Transactional
    @Query("SELECT e FROM EmprendedorEntity e")
    public List<EmprendedorEntity> findAllEmprendedores();
    
    @Transactional
    @Query("SELECT e FROM EmprendedorEntity e WHERE e.usuario.idUsu = ?1")
    public EmprendedorEntity findByUsuarioId(Long idUsuario);
    
    @Transactional
    @Query("SELECT e FROM EmprendedorEntity e WHERE e.estadoEmprendedor = ?1")
    public List<EmprendedorEntity> findByEstado(boolean estado);
    
    @Transactional
    @Query("SELECT e FROM EmprendedorEntity e WHERE e.sectorInteresEmprendedor = ?1")
    public List<EmprendedorEntity> findBySectorInteres(String sectorInteres);
    
    @Transactional
    @Modifying
    @Query("UPDATE EmprendedorEntity e SET e.estadoEmprendedor = ?2 WHERE e.idEmprendedor = ?1")
    public int actualizarEstadoEmprendedor(Long idEmprendedor, boolean estado);
    
    @Transactional
    @Modifying
    @Query("UPDATE EmprendedorEntity e SET e.experienciaEmprendedor = ?2, e.sectorInteresEmprendedor = ?3 WHERE e.idEmprendedor = ?1")
    public int actualizarDatosEmprendedor(Long idEmprendedor, String experiencia, String sectorInteres);
}
