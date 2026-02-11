package com.example.HaruDang.controller;

import com.example.HaruDang.dto.UserCreate;
import com.example.HaruDang.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreate userCreate) {
        userService.createUser(userCreate);
        return ResponseEntity.ok(Map.of("message", "Registered successfully"));
    }
}
