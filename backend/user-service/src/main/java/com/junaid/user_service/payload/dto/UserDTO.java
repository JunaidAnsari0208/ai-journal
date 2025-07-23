package com.junaid.user_service.payload.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for user registration.
 * This object carries the data from the user's request to the service layer.
 * It includes validation rules to ensure the data is clean before processing.
 */
@Data
public class    UserDTO {

    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String userName;

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
}
