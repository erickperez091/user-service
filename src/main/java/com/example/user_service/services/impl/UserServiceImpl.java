package com.example.user_service.services.impl;

import com.example.common.entity.MessageEvent;
import com.example.common.utilities.ConverterUtil;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.entity.User;
import com.example.user_service.messaging.UserPublisher;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.services.UserService;
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

    public UserDTO signup(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getUsername());
        optionalUser.ifPresentOrElse(user -> {
            throw new DuplicateKeyException("Username is already in use");
        }, () -> {
            userDTO.setId(UUID.randomUUID().toString());
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            Map<String, Object> userData = converterUtil.objectToMap(userDTO);
            MessageEvent event = new MessageEvent(CREATE_USER, userData);
            this.userPublisher.sendEvent(event);
            userDTO.setPassword("");
        });
        return userDTO;
    }
}
