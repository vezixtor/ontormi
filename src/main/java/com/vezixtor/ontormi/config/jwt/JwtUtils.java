package com.vezixtor.ontormi.config.jwt;

import com.vezixtor.ontormi.utils.TimeAgoUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public abstract class JwtUtils {
	private static final String SECRET = "PalmeirasNaoTemMundial00";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_ACCESS_TOKEN = "Access-Token";
    private static final String HEADER_REFRESH_TOKEN = "Refresh-Token";

	public static void addTokensOnHeader(HttpServletResponse response, String subject) {
		response.addHeader(HEADER_ACCESS_TOKEN, TOKEN_PREFIX + " " + buildAccessToken(subject));
		response.addHeader(HEADER_REFRESH_TOKEN, TOKEN_PREFIX + " " + buildRefreshToken(subject));
	}

	private static String buildAccessToken(String subject) {
		return Jwts.builder()
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeAgoUtils.DAY))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
	}

	private static String buildRefreshToken(String subject) {
		return Jwts.builder()
				.setSubject(subject)
				.claim("refresh", true)
				.setExpiration(new Date(System.currentTimeMillis() + (TimeAgoUtils.DAY * 2)))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
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
        return request.getHeader(HEADER_AUTHORIZATION).replace(TOKEN_PREFIX, "");
    }

    public static String getAuthorization(ServletRequest request) {
        return getAuthorization((HttpServletRequest) request);
    }
}
