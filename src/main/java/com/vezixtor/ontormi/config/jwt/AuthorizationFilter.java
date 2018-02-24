package com.vezixtor.ontormi.config.jwt;

import com.vezixtor.ontormi.exception.OntormiException;
import com.vezixtor.ontormi.utils.ExceptionResolverUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Collections;

public class AuthorizationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
		try {
			SecurityContextHolder.getContext().setAuthentication(this.getAuthentication(request));
			filterChain.doFilter(request, response);
		}
		catch (IOException | ServletException | IllegalArgumentException e) {
			ExceptionResolverUtils.restResponse(response, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
			ExceptionResolverUtils.restResponse(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		catch (NullPointerException | OntormiException e) {
			ExceptionResolverUtils.restResponse(response, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	private Authentication getAuthentication(ServletRequest request) {
		String token = JwtUtils.getAuthorization(request);
		if (token != null) {
			String user = JwtUtils.parser(token).getSubject();
			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
			}
		}
		return null;
	}
}
