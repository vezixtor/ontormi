package com.vezixtor.ontormi.config.jwt;

import com.vezixtor.ontormi.model.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class VeziAuthenticationToken extends AbstractAuthenticationToken {
    private final User user;
    private String jsonWebToken;

    public VeziAuthenticationToken(User user) {
        super(user.getAuthorities());
        this.user = user;
    }

    public VeziAuthenticationToken withToken(String jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
        return this;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials() {
        return jsonWebToken;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
