package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.ConvocatoriaEntity;
import com.usta.startupconnect.models.dao.ConvocatoriaDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConvocatoriaServiceImplement implements ConvocatoriaService {
    @Autowired
    private ConvocatoriaDao convocatoriaDao;

    @Autowired
    private NotificacionService notificacionService;

    @Override
    @Transactional()
    public List<ConvocatoriaEntity> findAll() {
        return (List<ConvocatoriaEntity>) convocatoriaDao.findAll();
    }

    @Override
    @jakarta.transaction.Transactional
    public void save(ConvocatoriaEntity convocatoria) {
        convocatoriaDao.save(convocatoria);
        notificacionService.notificarNuevaConvocatoriaATodosLosEmprendedores(
                "🚀 Nueva convocatoria publicada: " + convocatoria.getTitulo());
    }

    @Override
    @Transactional
    public ConvocatoriaEntity findById(Long id) {
        return convocatoriaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        convocatoriaDao.deleteById(id);
    }

}