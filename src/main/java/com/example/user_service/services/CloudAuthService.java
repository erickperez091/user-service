package com.example.user_service.services;

public interface CloudAuthService {

    String authenticateUser(String email, String password);

    void createUser(String email, String password);
}
