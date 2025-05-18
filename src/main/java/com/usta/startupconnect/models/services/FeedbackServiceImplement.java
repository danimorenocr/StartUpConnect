package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.FeedbackEntity;
import com.usta.startupconnect.models.dao.FeedbackDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedbackServiceImplement implements FeedbackService {
    @Autowired
    private FeedbackDao feedbackDao;

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackEntity> findAll() {
        return (List<FeedbackEntity>) feedbackDao.findAll();
    }
}