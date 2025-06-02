package com.usta.startupconnect.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.usta.startupconnect.handler.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/error", "/login", "/registro", "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                
                // Rutas de administrador
                .requestMatchers("/administrador", "/usuario/**", "/startup/**", "/startup/eliminar/**", "/entregable/actualizar/**").hasRole("ADMIN")
                
                // Rutas de mentor
                .requestMatchers("/mentor/**", "/mentor/detalle/**", "/mentor/feedback/**", "/etapa/**", "/convocatoria/detalles/**", "/feedback/crear/**").hasAnyRole("ADMIN", "MENTOR")
                  // Rutas de emprendedor
                .requestMatchers("/emprendedor/**", "/startup/crear/**", "/startup/editar/**", "/startup/eliminar/**", 
                                "/postulacion/**", "/startup/emprendedor/{documento}", "/crearStartupParaEmprendedor/**", 
                                "/misStartups").hasAnyRole("ADMIN", "EMPRENDEDOR")
                
                // Rutas compartidas
                .requestMatchers("/convocatoria/**", "/evento/**").hasAnyRole("ADMIN", "MENTOR", "EMPRENDEDOR")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/images/**", "/js/**", "/css/**", "/webjars/**", "/uploads/**"));
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

