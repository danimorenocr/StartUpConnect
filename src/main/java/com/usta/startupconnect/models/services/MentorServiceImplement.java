package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.MentorEntity;
import com.usta.startupconnect.models.dao.MentorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MentorServiceImplement implements MentorService {

    @Autowired
    private MentorDao mentorDao;

    @Override
    @Transactional(readOnly = true)
    public List<MentorEntity> findAll() {
        return (List<MentorEntity>) mentorDao.findAll();
    }

    @Override
    @jakarta.transaction.Transactional
    public void save(MentorEntity usuario) {
        mentorDao.save(usuario);
    }

    @Override
    @Transactional
    public MentorEntity findById(Long id) {
        return mentorDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        mentorDao.deleteById(id);
    }
}