package com.epam.training.controller;

import com.epam.training.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tokens")
public class TokenController {

    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<String> getToken() {
        return ResponseEntity.ok(jwtService.generateToken());
    }
}
