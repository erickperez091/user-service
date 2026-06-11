package com.example.user_service.controller.exception;

import com.example.common.exceptions.LoginAttemptExpiredException;
import com.example.user_service.exception.SessionAlreadyActiveException;
import com.example.user_service.services.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LoginAttemptService LoginAttemptService;

    @ExceptionHandler(SessionAlreadyActiveException.class)
    public ResponseEntity<?> handleSessionAlreadyActive(SessionAlreadyActiveException ex) {
        String message = ex.getMessage();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", message));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        this.LoginAttemptService.callUpdateLogingAttempt(username);
        logger.error("[GlobalExceptionHandler][handleBadCredentials][Start]: Invalid Credentials for User: {}", username);
        String message = String.format("Invalid Credentials for User: [%s]", username);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("error", message));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> handleLockedAccount(LockedException ex, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        logger.error("[GlobalExceptionHandler][handleLockedAccount][Start]: User Account Locked: {}", username);
        String message = String.format("User account: [%s] is locked", username);
        return ResponseEntity
                .status(HttpStatus.LOCKED)
                .body(Map.of("error", message));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<?> handleDuplicateUsername(DuplicateKeyException ex, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        logger.error("[GlobalExceptionHandler][handleDuplicateUsername][Start]: User already exists: {}", username);
        String message = String.format(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFound(UsernameNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(LoginAttemptExpiredException.class)
    public ResponseEntity<?> handleLoginAttemptExpired(LoginAttemptExpiredException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", message));
    }
}
