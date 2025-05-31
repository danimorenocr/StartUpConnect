package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.LikeEntity;
import com.usta.startupconnect.entities.StartupEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.dao.LikeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
    
    @Autowired
    private LikeDao likeDao;

    @Transactional
    public boolean toggleLike(StartupEntity startup, UsuarioEntity usuario) {
        LikeEntity existingLike = likeDao.findByStartupAndUsuario(startup, usuario);
        
        if (existingLike != null) {
            // Si ya existe un like, lo eliminamos
            likeDao.delete(existingLike);
            startup.setCantLikes(startup.getCantLikes() - 1);
            return false;
        } else {
            // Si no existe, creamos uno nuevo
            LikeEntity newLike = new LikeEntity();
            newLike.setStartup(startup);
            newLike.setUsuario(usuario);
            likeDao.save(newLike);
            startup.setCantLikes(startup.getCantLikes() + 1);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public boolean hasUserLiked(StartupEntity startup, UsuarioEntity usuario) {
        return likeDao.existsByStartupAndUsuario(startup, usuario);
    }
}
