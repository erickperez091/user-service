package com.example.user_service.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    @Email(message = "Invalid username, must be an email")
    private String username;
    private String password;
    private String role;
}
