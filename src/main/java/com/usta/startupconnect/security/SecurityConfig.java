package com.usta.startupconnect.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas
                .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/login", "/vitrina", "/registro", "/verStartup/**").permitAll()
                
                // Rutas para administradores
                .requestMatchers("/administrador/**", "/usuario/**").hasRole("ADMIN")
                
                // Rutas para mentores
                .requestMatchers("/mentor/**", "/etapa/**", "/feedback/**").hasAnyRole("ADMIN", "MENTOR")
                
                // Rutas para emprendedores
                .requestMatchers("/emprendedor/**", "/startup/**", "/postulacion/**").hasAnyRole("ADMIN", "EMPRENDEDOR")
                //ROL_ADMIN
                // Rutas compartidas
                .requestMatchers("/convocatoria/**", "/evento/**").hasAnyRole("ADMIN", "MENTOR", "EMPRENDEDOR")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") 
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );
        
        return http.build();
    }
    
    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        // Usar NoOpPasswordEncoder para permitir contraseñas en texto plano (NO USAR EN PRODUCCIÓN)
        return NoOpPasswordEncoder.getInstance();
    }
}

