package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.EmprendedorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmprendedorDao extends CrudRepository<EmprendedorEntity, String> {

  @Transactional
  @Query("SELECT e FROM EmprendedorEntity e")
  public List<EmprendedorEntity> findAllEmprendedores();

  @Transactional
  @Query("SELECT e FROM EmprendedorEntity e WHERE e.usuario = ?1")
  public EmprendedorEntity findByUsuarioId(Long idUsuario);

  @Transactional
  @Query("SELECT e FROM EmprendedorEntity e WHERE e.documento = ?1")
  public EmprendedorEntity findByDocumento(Long documento);

}
