package com.example.user_service.services;

import com.example.user_service.dto.LoginAttemptDTO;

public interface LoginAttemptService {

    void callUpdateLogingAttempt(String username);

    void updateLoginAttempt(LoginAttemptDTO loginAttemptDTO);
}
