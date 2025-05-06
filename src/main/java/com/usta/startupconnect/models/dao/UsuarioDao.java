package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.RolEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UsuarioDao extends CrudRepository<UsuarioEntity, Long> {



    @Transactional
    @Query("SELECT US FROM UsuarioEntity US WHERE US.emailUsu = ?1")
    public UsuarioEntity findByemail(String email);


}
