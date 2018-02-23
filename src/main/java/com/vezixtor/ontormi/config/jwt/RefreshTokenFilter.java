package com.vezixtor.ontormi.config.jwt;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vezixtor.ontormi.exception.OntormiException;
import com.vezixtor.ontormi.model.entity.User;
import com.vezixtor.ontormi.repository.UserRepository;
import com.vezixtor.ontormi.utils.ExceptionResolverUtils;
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
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
	private User user;
	private String jsonWebToken;
	private Claims accessClaims;
	private Claims refreshClaims;

	public RefreshTokenFilter(String antPattern, AuthenticationManager authManager, UserRepository userRepository) {
		super(new AntPathRequestMatcher(antPattern));
		setAuthenticationManager(authManager);
		this.userRepository = userRepository;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ExpiredJwtException {
		try {
			refreshProcessing(request);
			return new VeziAuthenticationToken(user).withToken(jsonWebToken);
		} catch (IOException e) {
			ExceptionResolverUtils.restResponse(response, e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
			ExceptionResolverUtils.restResponse(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (OntormiException e) {
			ExceptionResolverUtils.restResponse(response, e.getMessage(), e.getHttpStatus());
		}
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication auth) {
		JwtUtils.addTokensOnHeader(response, auth.getName());
	}

	private void refreshProcessing(HttpServletRequest request) throws IOException {
		setAccessClaims(request);
		setRefreshToken(request);
		refreshClaimsOrThrow();
		if (hasMatches()) {
			user = userRepository.findByEmail(refreshClaims.getSubject());
		}
	}

	private void refreshClaimsOrThrow() {
		if (!isRefreshToken(refreshClaims)) {
			throw new OntormiException("Refresh token invalido", HttpStatus.BAD_REQUEST);
		}
	}

	private void setAccessClaims(HttpServletRequest request) {
		String authorization = JwtUtils.getAuthorization(request);
		accessClaims = JwtUtils.forceParser(authorization);
	}

	private void setRefreshToken(HttpServletRequest request) throws IOException {
		try {
			Map map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
			jsonWebToken = map.get("refresh_token").toString();
			refreshClaims =  JwtUtils.parser(jsonWebToken);
		}
		catch (NullPointerException e) {
			throw new OntormiException("Params not found: refresh_token", HttpStatus.BAD_REQUEST);
		}
		catch (JsonMappingException e) {
			throw new OntormiException("No content body", HttpStatus.BAD_REQUEST);
		}
		catch (JsonParseException e) {
			throw new OntormiException("Unrecognized: Bad content body", HttpStatus.BAD_REQUEST);
		}
	}

	private Boolean isRefreshToken(Claims claims) {
		Boolean refresh = (Boolean) claims.get("refresh");
		return (refresh == null) ? false : refresh;
	}

	private boolean hasMatches() {
		return accessClaims.getSubject().equals(refreshClaims.getSubject())
				&& accessClaims.getIssuedAt().equals(refreshClaims.getIssuedAt());
	}
}
