package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.models.dao.MentorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MentorServiceImplement implements MentorService {

    @Autowired
    private MentorDao mentorDao;

    @Override
    @Transactional(readOnly = true)
    public List<MentorEntity> findAll() {
        return (List<MentorEntity>) mentorDao.findAll();
    }
}