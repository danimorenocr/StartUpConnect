package com.usta.startupconnect.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.usta.startupconnect.entities.RolEntity;
import com.usta.startupconnect.entities.UsuarioEntity;
import com.usta.startupconnect.models.dao.UsuarioDao;

import java.util.List;
import java.util.Collection;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UsuarioDao usuarioDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Still look up by email since users log in with email
        UsuarioEntity usuario = usuarioDao.findByEmail(username);

        if (usuario == null) {
            System.out.println("Usuario no encontrado: " + username);
            throw new UsernameNotFoundException("El usuario no existe");
        }

        System.out.println("Usuario encontrado: " + usuario.getEmailUsu());
        System.out.println("Documento: " + usuario.getDocumento());
        System.out.println("Contraseña almacenada (cifrada): " + usuario.getContrasenna());

        // Use CustomUserDetails to store both email and documento
        return new CustomUserDetails(
                usuario.getEmailUsu(), // Use email as username
                usuario.getContrasenna(),
                mapearAutoridadesRol(usuario.getRol()),
                usuario.getDocumento() // Store documento
        );
    }

    private Collection<? extends GrantedAuthority> mapearAutoridadesRol(RolEntity rol) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.getRol()));
    }

    public UsuarioEntity obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            String documento = ((CustomUserDetails) principal).getDocumento();
            return usuarioDao.findById(documento).orElse(null);
        }
        return null;
    }
}
