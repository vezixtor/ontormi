package com.vezixtor.ontormi.config;

import com.vezixtor.ontormi.config.jwt.AuthorizationFilter;
import com.vezixtor.ontormi.config.jwt.LoginFilter;
import com.vezixtor.ontormi.config.jwt.RefreshTokenFilter;
import com.vezixtor.ontormi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private String[] antPatternsPost = {"/api/users", "/api/auth/sign", "/api/auth/recover"};
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, antPatternsPost).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new LoginFilter("/api/auth", authenticationManager(), userRepository,
                                getBCryptPasswordEncoder()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new RefreshTokenFilter("/api/auth/token", authenticationManager(),
                        userRepository), UsernamePasswordAuthenticationFilter.class)
		        .addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring()
                .antMatchers(HttpMethod.POST, antPatternsPost);
    }
}
