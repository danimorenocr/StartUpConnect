// package com.usta.startupconnect.security;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;

// import com.usta.startupconnect.entities.RolEntity;
// import com.usta.startupconnect.entities.UsuarioEntity;
// import com.usta.startupconnect.models.dao.UsuarioDao;

// import java.util.List;
// import java.util.Collection;

// // @Service("jpaUserDetailsService")
// // public class JpaUserDetailsService implements UserDetailsService {
    
// //     @Autowired
// //     private UsuarioDao usuarioDao;

// //     @Override
// //     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
// //         UsuarioEntity usuario = usuarioDao.findByEmail(username);

// //         if (usuario == null) {
// //             System.out.println("Usuario no encontrado: " + username);
// //             throw new UsernameNotFoundException("El usuario no existe");
// //         }

// //         System.out.println("Usuario encontrado: " + usuario.getEmailUsu());
// //         System.out.println("Contraseña almacenada (cifrada): " + usuario.getContrasenna());

// //         return new User(
// //                 usuario.getEmailUsu(),
// //                 usuario.getContrasenna(),
// //                 mapearAutoridadesRol(usuario.getRol())
// //         );
// //     }

// //     private Collection<? extends GrantedAuthority> mapearAutoridadesRol(RolEntity rol) {
// //         return List.of(new SimpleGrantedAuthority("ROLE_" + rol.getRol()));
// //     }

// //     // Método para obtener el usuario autenticado actual
// //     public UsuarioEntity obtenerUsuarioAutenticado() {
// //         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
// //         String emailUsuario = authentication.getName();
// //         return usuarioDao.findByEmail(emailUsuario);
// //     }
// // }
