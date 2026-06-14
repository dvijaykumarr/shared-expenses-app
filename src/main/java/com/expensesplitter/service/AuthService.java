package com.expensesplitter.service;

import com.expensesplitter.dto.UserDTO;
import com.expensesplitter.request.LoginRequest;
import com.expensesplitter.response.AuthResponse;

public interface AuthService {

    public UserDTO register(UserDTO userDTO);
    AuthResponse login(LoginRequest request);
}
