package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.PostulacionEntity;
import com.usta.startupconnect.models.dao.PostulacionDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostulacionServiceImplement implements PostulacionService {
    @Autowired
    private PostulacionDao postulacionDao;

    @Override
    @Transactional(readOnly = true)
    public List<PostulacionEntity> findAll() {
        return (List<PostulacionEntity>) postulacionDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostulacionEntity> findByConvocatoria(ConvocatoriaEntity convocatoria) {
        return postulacionDao.findByConvocatoria(convocatoria);
    }

    @Override
    @Transactional
    public void save(PostulacionEntity postulacion) {
        postulacionDao.save(postulacion);
    }

    @Override
    @Transactional
    public PostulacionEntity findById(Long id) {
        return postulacionDao.findById(id).orElse(null);
    }    @Override
    @Transactional
    public void deleteById(Long id) {
        postulacionDao.deleteById(id);
    }
}