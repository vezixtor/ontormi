package com.vezixtor.ontormi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/escapes")
public class EscapeApiController {

    @GetMapping("/atrigger")
    public ResponseEntity<?> aTrigger() {
        return new ResponseEntity<>(LocalDateTime.now(), HttpStatus.OK);
    }
}
