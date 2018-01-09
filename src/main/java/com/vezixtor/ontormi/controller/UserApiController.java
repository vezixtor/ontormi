package com.vezixtor.ontormi.controller;

import com.vezixtor.ontormi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/greeting")
    public ResponseEntity<?> greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new ResponseEntity<>(userService.greeting(name), HttpStatus.OK);
    }

}
