package com.raghav.expense.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raghav.expense.dto.AuthResponse;
import com.raghav.expense.dto.LoginRequest;
import com.raghav.expense.dto.RegisterRequest;
import com.raghav.expense.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService)
    {
        this.userService=userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
         String result = userService.register(registerRequest);
         if(result.equals("User already exists")) {
            return ResponseEntity.status(409).body(result);}
         return ResponseEntity.status(201).body(result);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }
    
    
    
}
