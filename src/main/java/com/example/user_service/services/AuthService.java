package com.example.user_service.services;

import com.example.user_service.dto.LoginAttemptDTO;
import com.example.user_service.dto.TokenResponseDTO;
import jakarta.servlet.http.HttpServletRequest;


public interface AuthService {

    TokenResponseDTO login(String username, String password, HttpServletRequest request);

    TokenResponseDTO refreshToken(String token);

    void logout(String token);

    void updateLoginAttempt(LoginAttemptDTO loginAttemptDTO, String loginAttemptId);
}
