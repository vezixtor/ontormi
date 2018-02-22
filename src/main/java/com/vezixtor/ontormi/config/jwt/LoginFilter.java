package com.vezixtor.ontormi.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vezixtor.ontormi.exception.OntormiException;
import com.vezixtor.ontormi.model.entity.User;
import com.vezixtor.ontormi.repository.UserRepository;
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
import java.util.Map;
import java.util.Optional;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private Map credentials;
    private User user;

    public LoginFilter(String antPattern, AuthenticationManager authManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(new AntPathRequestMatcher(antPattern));
        setAuthenticationManager(authManager);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        try {
            setCredentialsOrThrow(request);
            setUserOrThrow();
            matchesPasswordOrThrow();
            return new VeziAuthenticationToken(user);
        }
        catch (NullPointerException | OntormiException e) {
            ExceptionResolverUtils.restResponse(response, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication auth) {
        JwtUtils.addTokensOnHeader(response, auth.getName());
    }

    private void setCredentialsOrThrow(HttpServletRequest request) throws IOException {
//        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), Map.class);
//        }
//        catch (JsonMappingException e) {
//            throw new BadRequestException("No content body");
//        }
//        catch (JsonParseException e) {
//            throw new BadRequestException("Unrecognized: Bad content body");
//        }
    }

    private void setUserOrThrow() {
        user = Optional.ofNullable(userRepository.findByEmail(credentials.get("email").toString()))
                .orElseThrow(() -> new OntormiException("Usuário e/ou senha incorreto(s)", HttpStatus.BAD_REQUEST));
    }

    private void matchesPasswordOrThrow() {
        if (!passwordEncoder.matches(credentials.get("password").toString(), user.getPassword())) {
            throw new OntormiException("Usuário e/ou senha incorreto(s)", HttpStatus.BAD_REQUEST);
        }
    }
}
