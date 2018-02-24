package com.vezixtor.ontormi.service;

import com.vezixtor.ontormi.config.jwt.JwtUtils;
import com.vezixtor.ontormi.exception.OntormiException;
import com.vezixtor.ontormi.model.entity.User;
import com.vezixtor.ontormi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
abstract class BaseService {
    @Autowired
    protected HttpServletRequest httpServletRequest;
    @Autowired
    protected UserRepository userRepository;

    boolean isSet(String value) {
        return value != null && !value.isEmpty();
    }

    boolean isSet(Object value) {
        return value != null;
    }

    boolean isSet(List<?> list) {
        return list != null && !list.isEmpty();
    }

    protected abstract String getNotFoundMessage();

    <T> T getOrThrow(T object, String message){
        return Optional.ofNullable(object).orElseThrow(() -> new OntormiException(message, HttpStatus.NOT_FOUND));
    }

    <T> T getOrThrow(T object){
        return getOrThrow(object, getNotFoundMessage());
    }

    protected User getUserFromToken() {
        String subject = JwtUtils.parser(httpServletRequest).getSubject();
        return getOrThrow(userRepository.findByEmail(subject));
    }
}
