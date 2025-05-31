package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.LikeEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeDao extends JpaRepository<LikeEntity, Long> {
    
    @Query("SELECT l FROM LikeEntity l WHERE l.startup = :startup AND l.usuario = :usuario")
    LikeEntity findByStartupAndUsuario(@Param("startup") StartupEntity startup, @Param("usuario") UsuarioEntity usuario);
    
    boolean existsByStartupAndUsuario(StartupEntity startup, UsuarioEntity usuario);
}