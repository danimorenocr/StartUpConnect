package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.MentorEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MentorDao extends CrudRepository<MentorEntity, String> {

    @Transactional(readOnly = true)
    @Query("SELECT m FROM MentorEntity m WHERE m.especialidad = ?1")
    List<MentorEntity> findByEspecialidad(String especialidad);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM MentorEntity m WHERE m.anosExperiencia >= ?1")
    List<MentorEntity> findByAnosExperienciaGreaterThanEqual(Short anosExperiencia);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM MentorEntity m WHERE m.biografia LIKE %?1%")
    List<MentorEntity> findByBiografiaContaining(String biografia);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM MentorEntity m WHERE m.linkedin LIKE %?1%")
    List<MentorEntity> findByLinkedinContaining(String linkedin);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM MentorEntity m WHERE m.usuario.documento = ?1")
    MentorEntity findByUsuarioDocumento(String documento);

}
