package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.EmprendedorEntity;
import com.usta.startupconnect.models.dao.EmprendedorDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmprendedorServiceImplement implements EmprendedorService {
    @Autowired
    private EmprendedorDao emprendedorDAO;

    @Override
    @Transactional
    public List<EmprendedorEntity> findAll() {
        return (List<EmprendedorEntity>) emprendedorDAO.findAll();
    }

    @Override
    @Transactional
    public void save(EmprendedorEntity emprendedor) {
        emprendedorDAO.save(emprendedor);
    }

    @Override
    @Transactional
    public EmprendedorEntity findById(Long id) {
        return emprendedorDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deletebyId(Long id) {
        emprendedorDAO.deleteById(id);
    }

    @Override
    @Transactional
    public EmprendedorEntity actualizar(EmprendedorEntity emprendedor) {
        return emprendedorDAO.save(emprendedor);
    }
    
    @Override
    @Transactional
    public EmprendedorEntity findByEmail(String email) {
        // El método findByEmail no existe en el DAO
        // Se podría implementar recorriendo todos los emprendedores y buscando por su usuario/email
        return null;
    }
}