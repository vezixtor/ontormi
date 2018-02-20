package com.vezixtor.ontormi.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class TokenAuthenticationService {

    static final int EXPIRATION_TIME_ACCESS_TOKEN = 1000;
    static final long EXPIRATION_TIME_REFRESH_TOKEN  = EXPIRATION_TIME_ACCESS_TOKEN + 1000;
    static final String SECRET = "PalmeirasNaoTemMundial00";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_AUTHORIZATION = "Authorization";
    static final String HEADER_ACCESS_TOKEN = "Access-Token";
    static final String HEADER_REFRESH_TOKEN = "Refresh-Token";
    static final String REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";

    static void addAuthentication(HttpServletResponse response, String username) {
        response.addHeader(HEADER_ACCESS_TOKEN, TOKEN_PREFIX + " " + buildAccessToken(username));
        response.addHeader(HEADER_REFRESH_TOKEN, TOKEN_PREFIX + " " + buildRefreshToken(username));
    }

    static String buildAccessToken(String subject) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(localDateTime.toInstant(ZoneOffset.UTC)))
                .setExpiration(Date.from(localDateTime.plusDays(1).toInstant(ZoneOffset.UTC)))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    static String buildRefreshToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("scopes", Arrays.asList(REFRESH_TOKEN))
                .claim("refresh", true)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_REFRESH_TOKEN))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_AUTHORIZATION);
        return authenticationFromToken(token);
    }

    static Authentication getRefreshToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_REFRESH_TOKEN);
        return authenticationFromToken(token);
    }

    static Authentication authenticationFromToken(String token) {
        if (token != null) {
            String user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            }
        }
        return null;
    }

}
