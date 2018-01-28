package com.vezixtor.ontormi.service;

import com.vezixtor.ontormi.exception.OntormiException;
import com.vezixtor.ontormi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
abstract class BaseService {
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

    <T> T get(T object, String key){
        return Optional.ofNullable(object).orElseThrow(() -> new OntormiException(key, HttpStatus.NOT_FOUND));
    }
}
