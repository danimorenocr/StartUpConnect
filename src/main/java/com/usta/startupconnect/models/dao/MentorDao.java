package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.MentorEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MentorDao extends CrudRepository<MentorEntity, Long> {

    @Transactional
    @Query("SELECT m FROM MentorEntity m")
    public List<MentorEntity> findAllMentores();
    
    @Transactional
    @Query("SELECT m FROM MentorEntity m WHERE m.usuario.idUsu = ?1")
    public MentorEntity findByUsuarioId(Long idUsuario);
    
    @Transactional
    @Query("SELECT m FROM MentorEntity m WHERE m.especialidadMentor = ?1")
    public List<MentorEntity> findByEspecialidad(String especialidad);
    
    @Transactional
    @Query("SELECT m FROM MentorEntity m WHERE m.estadoMentor = ?1")
    public List<MentorEntity> findByEstado(boolean estado);
    
    @Transactional
    @Modifying
    @Query("UPDATE MentorEntity m SET m.estadoMentor = ?2 WHERE m.idMentor = ?1")
    public int actualizarEstadoMentor(Long idMentor, boolean estado);
    
    @Transactional
    @Modifying
    @Query("UPDATE MentorEntity m SET m.especialidadMentor = ?2, m.experienciaMentor = ?3 WHERE m.idMentor = ?1")
    public int actualizarDatosMentor(Long idMentor, String especialidad, String experiencia);
}
