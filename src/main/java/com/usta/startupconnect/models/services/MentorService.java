package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.MentorEntity;

public interface MentorService {

    public List<MentorEntity> findAll();
    
    public void save(MentorEntity mentor);

    public MentorEntity findById(Long id);

    public void deleteById(Long id);


}
