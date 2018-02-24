package com.vezixtor.ontormi.controller;

import com.vezixtor.ontormi.model.dto.UserDTO;
import com.vezixtor.ontormi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> postCreate(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.postCreate(userDTO));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getRead() {
        return ResponseEntity.ok(userService.getRead());
    }

    @PutMapping
    public ResponseEntity<?> putUpdate(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.putUpdate(userDTO));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete() {
        userService.delete();
    }
}
