package com.vezixtor.ontormi.service;

import com.vezixtor.ontormi.domain.OntormiUser;
import com.vezixtor.ontormi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Object greeting(String name) {
        List<OntormiUser> ontormiUserList = userRepository.findByName(name);
        if (ontormiUserList.isEmpty()) {
            userRepository.save(new OntormiUser(name, "greeting"));
        }
        return userRepository.findAll();
    }

}
