package com.example.user_service.services;

import com.example.common.service.cache.CacheService;
import com.example.common.utilities.JwtUtils;
import com.example.user_service.dto.LoginAttemptDTO;
import com.example.user_service.dto.TokenResponseDTO;
import com.example.user_service.entity.User;
import com.example.user_service.exception.SessionAlreadyActiveException;
import com.example.user_service.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public interface AuthService {

    TokenResponseDTO login(String username, String password, HttpServletRequest request);

    TokenResponseDTO refreshToken(String token);

    void logout(String token);

    void updateLoginAttempt(LoginAttemptDTO loginAttemptDTO);
}
