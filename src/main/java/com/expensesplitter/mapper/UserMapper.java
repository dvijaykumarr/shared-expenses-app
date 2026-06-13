package com.expensesplitter.mapper;

import com.expensesplitter.dto.UserDTO;
import com.expensesplitter.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDTO dto) {

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        return user;
    }

    public UserDTO toDTO(User user) {

        UserDTO dto = new UserDTO();

        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        return dto;
    }
}
