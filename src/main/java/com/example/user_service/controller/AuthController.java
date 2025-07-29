package com.example.user_service.controller;

import com.example.user_service.dto.TokenResponseDTO;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/v1")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final AuthService authService;
    private final RedisBlackListService redisBlackListService;

    @PostMapping(name = "Sign Up", value = "/signup", path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> signup(@RequestBody UserDTO userDTO) {
        UserDTO userDTOResponse = this.authService.signup(userDTO);
        return new ResponseEntity<>(userDTOResponse, HttpStatus.CREATED);
    }

    @PostMapping(name = "Login", value = "/login", path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponseDTO> login(@RequestBody UserDTO userDTO) {
        TokenResponseDTO responseDTO = this.authService.login(userDTO.getUsername(), userDTO.getPassword());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(name = "Refresh Token", value = "/refresh-token", path = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponseDTO> refreshToken(@RequestHeader("Authorization") String token) {
        String tokenAux = token.substring(7);
        TokenResponseDTO responseDTO = this.authService.refreshToken(tokenAux);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(name = "Logout", value = "/logout", path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        String tokenAux = token.substring(7);
        this.redisBlackListService.addTokenToBlackList(tokenAux);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
