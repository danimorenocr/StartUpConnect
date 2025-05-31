package com.usta.startupconnect.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {
    private final String documento;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String documento) {
        super(username, password, authorities);
        this.documento = documento;
    }

    public String getDocumento() {
        return documento;
    }
}
