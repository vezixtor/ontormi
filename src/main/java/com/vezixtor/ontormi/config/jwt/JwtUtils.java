package com.vezixtor.ontormi.config.jwt;

import com.vezixtor.ontormi.exception.OntormiException;
import com.vezixtor.ontormi.utils.TimeUnits;
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;

public abstract class JwtUtils implements TimeUnits {
	private static final String SECRET = "RXxspMx4NkmJhfFFNh6zdsG3RXcdrHtv";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_ACCESS_TOKEN = "Access-Token";
    private static final String HEADER_REFRESH_TOKEN = "Refresh-Token";

	public static void addTokensOnHeader(HttpServletResponse response, String subject) {
		Date date = new Date(System.currentTimeMillis());
		response.addHeader(HEADER_ACCESS_TOKEN, TOKEN_PREFIX + buildAccessToken(subject, date).compact());
		response.addHeader(HEADER_REFRESH_TOKEN, TOKEN_PREFIX + buildRefreshToken(subject, date).compact());
	}

	private static JwtBuilder buildAccessToken(String subject, Date date) {
		return Jwts.builder()
				.setSubject(subject)
				.setIssuedAt(date)
				.setExpiration(plusDay(date, 7))
				.signWith(SignatureAlgorithm.HS512, getKeyDecoded());
	}

	private static JwtBuilder buildRefreshToken(String subject, Date date) {
		return Jwts.builder()
				.setSubject(subject)
				.claim("refresh", true)
				.setIssuedAt(date)
				.setExpiration(plusDay(date, 14))
				.signWith(SignatureAlgorithm.HS512, getKeyDecoded());
	}

	private static Date plusDay(Date date, int days) {
		return new Date(date.getTime() + (DAY * days));
	}

	public static Claims parser(String token) {
	    return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();
    }

	public static Claims forceParser(String token) {
        try {
            return parser(token);
        } catch(ExpiredJwtException e){
            return e.getClaims();
        }
    }

    public static String getAuthorization(HttpServletRequest request) {
		try {
			return request.getHeader(HEADER_AUTHORIZATION).replace(TOKEN_PREFIX, "");
		}
		catch (NullPointerException e) {
			throw new OntormiException("Header not found: Authorization", HttpStatus.BAD_REQUEST);
		}
    }

    public static String getAuthorization(ServletRequest request) {
        return getAuthorization((HttpServletRequest) request);
    }

	private static byte[] getKeyDecoded() {
		return Base64.getDecoder().decode(SECRET);
	}
}
