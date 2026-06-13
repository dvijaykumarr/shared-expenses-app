package com.expensesplitter.service.impl;

import com.expensesplitter.dto.UserDTO;
import com.expensesplitter.mapper.UserMapper;
import com.expensesplitter.model.User;
import com.expensesplitter.repository.UserRepository;
import com.expensesplitter.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDTO register(UserDTO userDTO) {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(userDTO);

        user.setPassword(
                passwordEncoder.encode(userDTO.getPassword())
        );

        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);

    }
}
