package com.vezixtor.ontormi.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vezixtor.ontormi.model.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JWTAuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
		try {
			Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest) request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(request, response);
		} catch (IOException | ServletException e) {
			exceptionToRestJson(response, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
                | IllegalArgumentException e) {
			String authorization = ((HttpServletRequest) request).getHeader("Authorization");
			System.out.println(authorization);
			System.out.println(
			        LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss[.SSS]  ")) + e.getClass().getName()
            );
			exceptionToRestJson(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	private void exceptionToRestJson(ServletResponse response, String message, HttpStatus httpStatus) {
        try {
        	HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        	httpServletResponse.setStatus( httpStatus.value() );
        	response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//        	response.setCharacterEncoding(charset);
			response.getWriter().write(convertObjectToJson(new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), message)));
		} catch (IOException e) {
			exceptionToRestJson(response, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private String convertObjectToJson(Object object) throws IOException {
        if (object == null) {
            return null;
        }
        return new ObjectMapper().writeValueAsString(object);
    }
}
