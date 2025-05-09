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
    private MentorDao mentorDAO;

    @Override
    @Transactional
    public List<MentorEntity> findAll() {
        return (List<MentorEntity>) mentorDAO.findAll();
    }

    @Override
    @Transactional
    public void save(MentorEntity mentor) {
        mentorDAO.save(mentor);
    }

    @Override
    @Transactional
    public MentorEntity findById(Long id) {
        return mentorDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deletebyId(Long id) {
        mentorDAO.deleteById(id);
    }

    @Override
    @Transactional
    public MentorEntity actualizar(MentorEntity mentor) {
        return mentorDAO.save(mentor);
    }
    
    @Override
    @Transactional
    public MentorEntity findByEmail(String email) {
        // El método findByEmail no existe en el DAO
        // Solución temporal: buscar por otros medios o devolver null
        return null;
    }
}