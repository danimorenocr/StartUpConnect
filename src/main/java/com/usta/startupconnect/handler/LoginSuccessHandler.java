package com.usta.startupconnect.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import jakarta.servlet.ServletException;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    
    public LoginSuccessHandler() {
        super();
        // No usar una request guardada si es recurso estático
        setAlwaysUseDefaultTargetUrl(true);
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
        FlashMap flashMap = new FlashMap();
        flashMap.put("success", "Has iniciado sesión correctamente, " + authentication.getName());
        flashMapManager.saveOutputFlashMap(flashMap, request, response);
        
        // Determinar la URL de redirección basada en el rol del usuario
        String redirectUrl;
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            redirectUrl = "/administrador";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MENTOR"))) {
            redirectUrl = "/mentor/dashboardMentor";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPRENDEDOR"))) {
            redirectUrl = "/emprendedor/dashboardEmprendedor";
        } else {
            // Redirección por defecto si no se identifica un rol específico
            redirectUrl = "/";
        }
        
        // Establecer la URL de redirección como destino por defecto
        setDefaultTargetUrl(redirectUrl);
        
        // Limpiar cualquier solicitud guardada que pueda ser un recurso estático
        clearAuthenticationAttributes(request);
        
        // Redirigir al usuario
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
