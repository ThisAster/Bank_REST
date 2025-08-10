package com.example.bankcards.security;

import com.example.bankcards.entity.User;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class JwtAuthentication implements Authentication {

    private boolean authenticated;
    private User principal;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return principal.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return principal.getId();
    }

    @Override
    public Object getDetails() {
        return principal.getId();
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal.getUsername();
    }
}
