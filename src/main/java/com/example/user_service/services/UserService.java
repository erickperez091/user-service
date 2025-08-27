package com.example.user_service.services;

import com.example.user_service.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    UserDTO signup(UserDTO userDTO, HttpServletRequest request);
}
