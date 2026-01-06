package com.example.user_service.services.impl;

import com.example.common.service.cache.CacheService;
import com.example.common.utilities.JwtUtils;
import com.example.user_service.dto.LoginAttemptDTO;
import com.example.user_service.dto.TokenResponseDTO;
import com.example.user_service.entity.User;
import com.example.user_service.exception.SessionAlreadyActiveException;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.services.AuthService;
import com.example.user_service.services.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    private final CacheService cacheService;
    private final LoginAttemptService loginAttemptService;
    @Qualifier(value = "customAuthenticationManager")
    private final AuthenticationManager authenticationManager;

    public TokenResponseDTO login(String username, String password, HttpServletRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            String isSessionActive = this.cacheService.isSessionActive(username);
            if (Objects.nonNull(isSessionActive) && !this.cacheService.isTokenInBlackList(isSessionActive)) {
                throw new SessionAlreadyActiveException("Session is active");
            }
            String role = auth.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("USER");
            String token = this.jwtUtils.generateToken(username, Map.of("role", role));
            String refreshToken = this.jwtUtils.generateRefreshToken(username);
            cacheService.storeActiveToken(username, token);
            return new TokenResponseDTO(token);
        } catch (BadCredentialsException | LockedException | UsernameNotFoundException ex) {
            request.setAttribute("username", username);
            throw ex;
        }
    }

    public TokenResponseDTO refreshToken(String token) {
        String username = jwtUtils.getUsername(token);
        Optional<User> optionalUser = userRepository.findByUsername(username);
        final StringBuilder refreshToken = new StringBuilder();
        optionalUser.ifPresent(user -> {
            refreshToken.append(jwtUtils.generateToken(username, Map.of("role", user.getRole())));
        });
        return new TokenResponseDTO(refreshToken.toString());
    }

    public void logout(String token) {
        String tokenAux = token.substring(7);
        this.cacheService.addTokenToBlackList(tokenAux);
    }

    public void updateLoginAttempt(LoginAttemptDTO loginAttemptDTO) {
        this.loginAttemptService.updateLoginAttempt(loginAttemptDTO);
    }
}
