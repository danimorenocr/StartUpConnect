package com.usta.startupconnect.models.services;

import java.util.Date;
import java.util.List;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.entities.PostulacionEntity;
import com.usta.startupconnect.entities.StartupEntity;

public interface PostulacionService {
    public List<PostulacionEntity> findAll();


}
