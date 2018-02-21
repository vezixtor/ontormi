package com.vezixtor.ontormi.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vezixtor.ontormi.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RefreshTokenFilter extends AbstractAuthenticationProcessingFilter {
	private UserRepository userRepository;

	public RefreshTokenFilter(String antPattern, AuthenticationManager authManager, UserRepository userRepository) {
		super(new AntPathRequestMatcher(antPattern));
		setAuthenticationManager(authManager);
		this.userRepository = userRepository;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ExpiredJwtException {
		Map map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
        String authorization = JwtUtils.getAuthorization(request);
        String accessSubject = JwtUtils.forceParser(authorization).getSubject();
		String refreshSubject = JwtUtils.parser(map.get("refresh_token").toString()).getSubject();
		if (accessSubject.equals(refreshSubject)) {
			System.out.println("always gonna be okay");
		}
		return accessSubject.equals(refreshSubject) ? null : null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication auth) {
		System.out.println("successfulAuthentication => refreshed");
		JwtUtils.addTokensOnHeader(response, auth.getName());
	}
}
