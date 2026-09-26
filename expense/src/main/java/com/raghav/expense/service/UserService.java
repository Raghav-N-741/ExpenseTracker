package com.raghav.expense.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.raghav.expense.dto.AuthResponse;
import com.raghav.expense.dto.LoginRequest;
import com.raghav.expense.dto.RegisterRequest;
import com.raghav.expense.model.User;
import com.raghav.expense.repository.UserRepository;

@Service 
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager,JwtService jwtService)
    {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
    }

    public String register(RegisterRequest request)
    {
        if(!userRepository.findByEmail(request.getEmail()).isEmpty())
        {
            return "User already exists";
        }
        String hashedpwd=passwordEncoder.encode(request.getPassword());
        User user=new User(request.getUsername(),request.getEmail(),hashedpwd);
        userRepository.save(user);
        return "User created successfully";
    }

    public AuthResponse login(LoginRequest request)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        return  new AuthResponse(jwtService.generateToken(request.getEmail()));
    }

}
