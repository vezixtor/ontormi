package com.vezixtor.ontormi.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vezixtor.ontormi.exception.OntormiException;
import com.vezixtor.ontormi.model.entity.User;
import com.vezixtor.ontormi.repository.UserRepository;
import com.vezixtor.ontormi.utils.CryptUtils;
import com.vezixtor.ontormi.utils.ExceptionResolverUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    private Map credentials;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public LoginFilter(String antPattern, AuthenticationManager authManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(new AntPathRequestMatcher(antPattern));
        setAuthenticationManager(authManager);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        credentials = new ObjectMapper().readValue(request.getInputStream(), Map.class);
        User user = getUserOrThrow();
        matchesPasswordOrThrow(response, user);
        return new VeziAuthenticationToken(user);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication auth) {
        JwtUtils.addTokensOnHeader(response, auth.getName());
    }

    private User getUserOrThrow() {
        return Optional.ofNullable(userRepository.findByEmail(credentials.get("email").toString()))
                .orElseThrow(() -> new OntormiException("Usuário e/ou senha incorreto(s)", HttpStatus.BAD_REQUEST));
    }

    private void matchesPasswordOrThrow(HttpServletResponse response, User user) {
        try {
            if (!hasMatchesPassword(credentials.get("password").toString(), user)) {
                throw new OntormiException("Usuário e/ou senha incorreto(s)", HttpStatus.BAD_REQUEST);
            }
        }
        catch (NoSuchAlgorithmException e) {
            ExceptionResolverUtils.restResponse(response, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (OntormiException e) {
            ExceptionResolverUtils.restResponse(response, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private boolean hasMatchesPassword(String password, User user) throws NoSuchAlgorithmException {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            if (user.getPassword().equals(CryptUtils.toMd5(password))) {
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
            } else {
                return false;
            }
        }
        return true;
    }
}
