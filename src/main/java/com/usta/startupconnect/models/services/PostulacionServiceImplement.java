package com.usta.startupconnect.models.services;

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

}