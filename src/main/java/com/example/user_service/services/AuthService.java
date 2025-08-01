package com.example.user_service.services;

import com.example.common.service.RedisService;
import com.example.common.utilities.JwtUtils;
import com.example.user_service.dto.LoginAttemptDTO;
import com.example.user_service.dto.TokenResponseDTO;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.entity.RoleEnum;
import com.example.user_service.entity.User;
import com.example.user_service.exception.SessionAlreadyActiveException;
import com.example.user_service.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final LoginAttemptService loginAttemptService;
    private final AuthenticationManager authenticationManager;

    public UserDTO signup(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getUsername());
        optionalUser.ifPresentOrElse(user -> {
            throw new DuplicateKeyException("Username is already in use");
        }, () -> {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setUsername(userDTO.getUsername());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setRole(RoleEnum.valueOf(userDTO.getRole()));
            this.userRepository.save(user);
            userDTO.setPassword("");
        });
        return userDTO;
    }

    public TokenResponseDTO login(String username, String password, HttpServletRequest request) {

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            String isSessionActive = this.redisService.isSessionActive(username);
            if (Objects.nonNull(isSessionActive) && !this.redisService.isTokenInBlackList(isSessionActive)) {
                throw new SessionAlreadyActiveException("Session is active");
            }
            String role = auth.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("USER");
            String token = jwtUtils.generateToken(username, Map.of("role", role));
            redisService.storeActiveToken(username, token);
            return new TokenResponseDTO(token);
        } catch (BadCredentialsException | LockedException ex) {
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
        this.redisService.addTokenToBlackList(tokenAux);
    }

    public void updateLoginAttempt(LoginAttemptDTO loginAttemptDTO) {
        this.loginAttemptService.updateLoginAttempt(loginAttemptDTO);
    }
}
