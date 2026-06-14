package com.expensesplitter.service.impl;

import com.expensesplitter.dto.UserDTO;
import com.expensesplitter.mapper.UserMapper;
import com.expensesplitter.model.User;
import com.expensesplitter.repository.UserRepository;
import com.expensesplitter.request.LoginRequest;
import com.expensesplitter.response.AuthResponse;
import com.expensesplitter.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;



    @Override
    public UserDTO register(UserDTO userDTO) {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(userDTO);

        user.setPassword(
                BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt())
        );

        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);

    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        boolean passwordMatches =
                BCrypt.checkpw(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {

            throw new RuntimeException("Invalid email or password");
        }

        return new AuthResponse("Login successful");
    }
}
