package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.EtapaEntity;
import com.usta.startupconnect.entities.TareaEntity;
import com.usta.startupconnect.models.dao.TareaDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
public class TareaServiceImplement implements TareaService {
    @Autowired
    private TareaDao tareaDAO;

    @Override
    @Transactional
    public List<TareaEntity> findAll() {
        return (List<TareaEntity>) tareaDAO.findAll();
    }

    @Override
    @Transactional
    public void save(TareaEntity tarea) {
        tareaDAO.save(tarea);
    }

    @Override
    @Transactional
    public TareaEntity findById(Long id) {
        return tareaDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        tareaDAO.deleteById(id);
    }

    @Override
    @Transactional
    public TareaEntity actualizar(TareaEntity tarea) {
        return tareaDAO.save(tarea);
    }
    
    @Override
    @Transactional
    public List<TareaEntity> findByTituloContaining(String titulo) {
        return tareaDAO.findByTitulo(titulo);
    }
    
    @Override
    @Transactional
    public List<TareaEntity> findByEtapa(EtapaEntity etapa) {
        return tareaDAO.findByEtapaId(etapa.getId());
    }
    
    @Override
    @Transactional
    public List<TareaEntity> findByFechaEntregaBefore(Date fecha) {
        return tareaDAO.findTareasPendientesPorVencer(fecha);
    }
    
    @Override
    @Transactional
    public List<TareaEntity> findByFechaEntregaAfter(Date fecha) {
        // This might need a custom query in TareaDao
        return null;
    }
    
    @Override
    @Transactional
    public List<TareaEntity> findByFechaEntregaBetween(Date fechaInicio, Date fechaFin) {
        // This might need a custom query in TareaDao
        return null;
    }
    
    @Override
    @Transactional
    public List<TareaEntity> findPendingTareas(Date fechaActual) {
        return tareaDAO.findTareasPendientesPorVencer(fechaActual);
    }
    
    @Override
    @Transactional
    public List<TareaEntity> findCompletedTareas(Date fechaActual) {
        // This might need a custom query in TareaDao
        return null;
    }
    
    @Override
    @Transactional
    public Long countByEtapa(EtapaEntity etapa) {
        // This might need a custom query in TareaDao
        return null;
    }
}