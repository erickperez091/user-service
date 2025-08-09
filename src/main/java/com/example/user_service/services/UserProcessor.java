package com.example.user_service.services;

import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class UserProcessor {

    private final UserRepository userRepository;

    public void saveUser(User user){
        logger.info("[UserProcessor][saveUser][Start] Saving User");
        this.userRepository.save(user);
        logger.info("[UserProcessor][saveUser][End] Saving User");
    }
}
