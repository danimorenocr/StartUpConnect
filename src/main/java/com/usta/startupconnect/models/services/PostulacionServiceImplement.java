package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.PostulacionEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.models.dao.PostulacionDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PostulacionServiceImplement implements PostulacionService {
    @Autowired
    private PostulacionDao postulacionDAO;

    @Override
    @Transactional
    public List<PostulacionEntity> findAll() {
        return (List<PostulacionEntity>) postulacionDAO.findAll();
    }

    @Override
    @Transactional
    public void save(PostulacionEntity postulacion) {
        postulacionDAO.save(postulacion);
    }

    @Override
    @Transactional
    public PostulacionEntity findById(Long id) {
        return postulacionDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        postulacionDAO.deleteById(id);
    }

    @Override
    @Transactional
    public PostulacionEntity actualizar(PostulacionEntity postulacion) {
        return postulacionDAO.save(postulacion);
    }
    
    @Override
    @Transactional
    public List<PostulacionEntity> findByStartup(StartupEntity startup) {
        return postulacionDAO.findByStartupId(startup.getId());
    }
    
    @Override
    @Transactional
    public List<PostulacionEntity> findByConvocatoria(ConvocatoriaEntity convocatoria) {
        return postulacionDAO.findByConvocatoriaId(convocatoria.getId());
    }
    
    @Override
    @Transactional
    public List<PostulacionEntity> findByEstado(String estado) {
        return postulacionDAO.findByEstado(estado);
    }
    
    @Override
    @Transactional
    public List<PostulacionEntity> findByNombreProyectoContaining(String nombre) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver null o lista vacía
        return null;
    }
    
    @Override
    @Transactional
    public List<PostulacionEntity> findByEtapaProyecto(String etapa) {
        // Este método no está disponible en el DAO
        // Implementación temporal: devolver null o lista vacía
        return null;
    }
    
    @Override
    @Transactional
    public List<PostulacionEntity> findByFechaPostulacionBetween(Date fechaInicio, Date fechaFin) {
        return postulacionDAO.findByFechaBetween(fechaInicio, fechaFin);
    }
    
    @Override
    @Transactional
    public Long countByConvocatoria(ConvocatoriaEntity convocatoria) {
        // Este método no está disponible en el DAO
        // Implementación temporal utilizando el método find y contando el resultado
        List<PostulacionEntity> postulaciones = findByConvocatoria(convocatoria);
        return postulaciones != null ? Long.valueOf(postulaciones.size()) : 0L;
    }
    
    @Override
    @Transactional
    public Long countByStartup(StartupEntity startup) {
        // Este método no está disponible en el DAO
        // Implementación temporal utilizando el método find y contando el resultado
        List<PostulacionEntity> postulaciones = findByStartup(startup);
        return postulaciones != null ? Long.valueOf(postulaciones.size()) : 0L;
    }
}