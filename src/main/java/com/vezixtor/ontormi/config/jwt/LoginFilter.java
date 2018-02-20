package com.vezixtor.ontormi.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vezixtor.ontormi.exception.OntormiException;
import com.vezixtor.ontormi.model.entity.User;
import com.vezixtor.ontormi.repository.UserRepository;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    private AccountCredentials credentials;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

	public LoginFilter(String antPattern, AuthenticationManager authManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super(new AntPathRequestMatcher(antPattern));
		setAuthenticationManager(authManager);
        this.userRepository = userRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        credentials = new ObjectMapper().readValue(request.getInputStream(), AccountCredentials.class);
        User user = null;
        try {
            user = getUserOrThrow();
            matchesPasswordOrThrow(user);
        } catch (OntormiException e) {
            System.out.println(e.getMessage());
        }
        return new VeziAuthenticationToken(user);
    }

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication auth) {
		System.out.println("successfulAuthentication => logado");
		TokenAuthenticationService.addAuthentication(response, auth.getName());
	}

	private User getUserOrThrow() {
        return Optional.ofNullable(userRepository.findByEmail(credentials.getEmail()))
                .orElseThrow(() -> new OntormiException("Usuário e/ou senha incorreto(s)", HttpStatus.NOT_FOUND));
    }

	private void matchesPasswordOrThrow(User user) {
        if (!hasMatchesPassword(credentials.getPassword(), user)) {
            throw new OntormiException("Usuário e/ou senha incorreto(s)", HttpStatus.NOT_FOUND);
        }
    }

    private boolean hasMatchesPassword(String password, User user) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            if (user.getPassword().equals(toMd5(password))) {
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
            } else {
                return false;
            }
        }
        return true;
    }

    private String toMd5(String password) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new OntormiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        StringBuilder stringBuffer = new StringBuilder();
        for (byte b : digest) {
            stringBuffer.append(String.format("%02x", b & 0xff));
        }
        return stringBuffer.toString();
    }
}
