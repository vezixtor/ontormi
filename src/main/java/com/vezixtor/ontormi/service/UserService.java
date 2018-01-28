package com.vezixtor.ontormi.service;

import com.vezixtor.ontormi.exception.OntormiException;
import com.vezixtor.ontormi.model.dto.UserDTO;
import com.vezixtor.ontormi.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends BaseService {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO postCreate(UserDTO userDTO) {
        validPasswordOrElseThrow(userDTO.getPassword());
        ifUserIsPresentThrow(userDTO.getEmail());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User newUser = userRepository.save(new User(userDTO));
        userDTO.setPassword(null);
        return new UserDTO(newUser);
    }

    private void validPasswordOrElseThrow(String password) {
        if (!isSet(password) || password.length() < 6) {
            throw new OntormiException("invalid password", HttpStatus.BAD_REQUEST);
        }
    }

    private void ifUserIsPresentThrow(String email) {
        Optional.ofNullable(userRepository.findByEmail(email)).ifPresent(userPresent -> {
            throw new OntormiException("Email already exists", HttpStatus.CONFLICT);
        });
    }

    public UserDTO getRead() {
        Long mockedId = 1L; //TODO remove mock
        User user = get(userRepository.findOne(mockedId), "User not found");
        return new UserDTO(user);
    }

    public UserDTO putUpdate(UserDTO userDTO) {
        Long mockedId = 1L; //TODO remove mock
        User user = get(userRepository.findOne(mockedId), "User not found");
        if (!user.getEmail().equals(userDTO.getEmail())) {
            ifUserIsPresentThrow(userDTO.getEmail());
        }
        User updateUser = userRepository.save(user.update(userDTO));
        return new UserDTO(updateUser);
    }

    public void delete() {
        Long mockedId = 3L; //TODO remove mock
        User user = get(userRepository.findOne(mockedId), "User not found");
        userRepository.save(user.delete());
    }
}
