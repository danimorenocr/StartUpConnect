package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.models.dao.EtapaDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class EtapaServiceImplement implements EtapaService {
    @Autowired
    private EtapaDao etapaDao;

    @Override
    @Transactional(readOnly = true)
    public List<EtapaEntity> findAll() {
        return (List<EtapaEntity>) etapaDao.findAll();
    }

}