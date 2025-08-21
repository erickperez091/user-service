package com.example.user_service.services;

import com.example.user_service.dto.UserDTO;

public interface UserService {
    UserDTO signup(UserDTO userDTO);
}
