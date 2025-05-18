package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.ComentariosEntity;
import com.usta.startupconnect.models.dao.ComentariosDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComentariosServiceImplement implements ComentariosService {

    @Autowired
    private ComentariosDao comentariosDao;

    @Override
    @Transactional(readOnly = true)
    public List<ComentariosEntity> findAll() {
        return (List<ComentariosEntity>) comentariosDao.findAll();
    }


} 