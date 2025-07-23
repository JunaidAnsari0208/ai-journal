package com.junaid.user_service.controller;

import com.junaid.user_service.model.User;
import com.junaid.user_service.payload.dto.UserResponseDTO;
import com.junaid.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> allUsers = userService.findAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId){
        return userService.findById(userId)
                .map(user -> {
                    // Convert the User entity to the safe DTO
                    UserResponseDTO responseDto = new UserResponseDTO();
                    responseDto.setId(user.getId());
                    responseDto.setUserName(user.getUsername()); // Note: getUsername() from UserDetails
                    responseDto.setEmail(user.getEmail());
                    return ResponseEntity.ok(responseDto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) throws Exception {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
