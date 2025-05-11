package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.RolEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RolDao extends CrudRepository<RolEntity, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT r FROM RolEntity r WHERE r.rol = ?1")
    RolEntity findByRol(String rol);

    @Transactional(readOnly = true)
    @Query("SELECT r FROM RolEntity r WHERE r.rol LIKE %?1%")
    List<RolEntity> findByRolContaining(String rol);

    @Transactional(readOnly = true)
    @Query("SELECT COUNT(r) FROM RolEntity r")
    Long countRoles();
}
