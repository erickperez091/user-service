package com.example.user_service.controller;

import com.example.user_service.dto.LoginAttemptDTO;
import com.example.user_service.dto.TokenResponseDTO;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.services.AuthService;
import com.example.user_service.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping(name = "Sign Up", value = "/signup", path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> signup(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        UserDTO userDTOResponse = this.userService.signup(userDTO, request);
        return new ResponseEntity<>(userDTOResponse, HttpStatus.CREATED);
    }

    @PostMapping(name = "Login", value = "/login", path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponseDTO> login(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        TokenResponseDTO responseDTO = this.authService.login(userDTO.getUsername(), userDTO.getPassword(), request);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(name = "Refresh Token", value = "/refresh-token", path = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponseDTO> refreshToken(@RequestHeader("Authorization") String token) {
        String tokenAux = token.substring(7);
        TokenResponseDTO responseDTO = this.authService.refreshToken(tokenAux);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(name = "Logout", value = "/logout", path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<com.example.common.entity.ResponseEntity> logout(@RequestHeader("Authorization") String token) {
        this.authService.logout(token);
        com.example.common.entity.ResponseEntity responseEntity = new com.example.common.entity.ResponseEntity(HttpStatus.OK.value(), null, "Logout successfully!");
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @PostMapping(name = "Login Attempt", value = "/login-attempt", path = "/login-attempt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> loginAttempt(@RequestHeader("X-Internal-Key") String internalKey, @RequestBody LoginAttemptDTO loginAttemptDTO) {
        logger.info("[AuthController][loginAttempt][Start] Internal Api Key {}", internalKey);
        this.authService.updateLoginAttempt(loginAttemptDTO, internalKey);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
