package com.example.user_service.services;

import com.example.common.utilities.JwtUtils;
import com.example.user_service.dto.TokenResponseDTO;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.entity.RoleEnum;
import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

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

    public TokenResponseDTO login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        return new TokenResponseDTO(jwtUtils.generateToken(username, Map.of("role", user.getRole())));
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

    /*private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserDTO signup(UserDTO userDTO) {
        RoleEnum role = RoleEnum.valueOf(userDTO.getRole());
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(role);
        this.userRepository.save(user);
        userDTO.setPassword("");
        return userDTO;
    }

    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String token = jwtService.generateToken(org.springframework.security.core.userdetails.User.builder().username(username).password(password).authorities(authentication.getAuthorities()).build());
        return token;
    }

    public String refreshToken(String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return this.jwtService.generateToken(org.springframework.security.core.userdetails.User.builder().username(username).password(user.getPassword()).authorities(user.getRole().toString()).build());
    }*/
}
