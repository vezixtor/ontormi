package com.vezixtor.ontormi.config.jwt;

import com.vezixtor.ontormi.model.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class VeziAuthenticationToken extends AbstractAuthenticationToken {
    private final User user;

    VeziAuthenticationToken(User user) {
        super(user.getAuthorities());
        this.user = user;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
