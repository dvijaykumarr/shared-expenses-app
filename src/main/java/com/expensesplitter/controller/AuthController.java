package com.expensesplitter.controller;

import com.expensesplitter.dto.UserDTO;
import com.expensesplitter.request.LoginRequest;
import com.expensesplitter.response.AuthResponse;
import com.expensesplitter.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserDTO register(@Valid @RequestBody UserDTO userDTO) {

        return authService.register(userDTO);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {

        return authService.login(request);
    }
}
