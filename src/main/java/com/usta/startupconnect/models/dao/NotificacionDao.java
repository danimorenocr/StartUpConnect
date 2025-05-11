package com.usta.startupconnect.models.dao;

import com.usta.startupconnect.entities.NotificacionEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface NotificacionDao extends CrudRepository<NotificacionEntity, Long> {

}
