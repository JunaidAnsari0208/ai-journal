package com.junaid.user_service.payload.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String userName;
    private String email;
}
