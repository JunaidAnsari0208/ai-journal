package com.junaid.user_service.service;

import com.junaid.user_service.model.User;
import com.junaid.user_service.payload.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService{
    User registerUser(UserDTO userDTO);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long userId);
    List<User> findAllUsers();
    void deleteUser(Long userId) throws Exception;
}
