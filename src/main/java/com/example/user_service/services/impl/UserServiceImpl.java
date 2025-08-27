package com.example.user_service.services.impl;

import com.example.common.entity.MessageEvent;
import com.example.common.utilities.ConverterUtil;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.entity.User;
import com.example.user_service.messaging.UserPublisher;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.example.common.entity.EnumUtil.EventType.CREATE_USER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ConverterUtil converterUtil;
    private final UserPublisher userPublisher;

    public UserDTO signup(UserDTO userDTO, HttpServletRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getUsername());
        optionalUser.ifPresentOrElse(user -> {
            request.setAttribute("username", userDTO.getUsername());
            throw new DuplicateKeyException(String.format("User [%s] already exists", userDTO.getUsername()));
        }, () -> {
            userDTO.setId(UUID.randomUUID().toString());;
            Map<String, Object> userData = converterUtil.objectToMap(userDTO);
            MessageEvent event = new MessageEvent(CREATE_USER, userData);
            this.userPublisher.sendEvent(event);
            userDTO.setPassword("");
        });
        return userDTO;
    }
}
