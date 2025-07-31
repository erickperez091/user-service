package com.example.user_service.exception;

public class SessionAlreadyActiveException extends RuntimeException{

    public SessionAlreadyActiveException(String message) {
        super(message);
    }
}
