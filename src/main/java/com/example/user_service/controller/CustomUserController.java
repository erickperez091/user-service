package com.example.user_service.controller;

import com.example.user_service.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/user/v1" )
public class CustomUserController {

    @PostMapping(name="Create User", value = "/", path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser( UserDTO userDTO ) {
        return new ResponseEntity<>( HttpStatus.CREATED );
    }
}
