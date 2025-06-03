package com.usta.startupconnect.models.services;

import com.usta.startupconnect.entities.UsuarioEntity;

public interface NotificacionService {
    void notificarEnSegundoPlano(String mensaje, String tipoEntidad, Long entidadId, UsuarioEntity usuario);

    void notificarNuevaConvocatoriaATodosLosEmprendedores(String mensaje);

}