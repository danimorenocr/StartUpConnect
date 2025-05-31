package com.usta.startupconnect.models.services;

import java.util.List;

import com.usta.startupconnect.entities.FeedbackEntity;

public interface FeedbackService {
    public List<FeedbackEntity> findAll();
    public FeedbackEntity save(FeedbackEntity feedback);
    public FeedbackEntity findById(Long id);
}
