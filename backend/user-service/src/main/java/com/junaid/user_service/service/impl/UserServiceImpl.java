package com.junaid.user_service.service.impl;

import com.junaid.user_service.model.Role;
import com.junaid.user_service.model.User;
import com.junaid.user_service.payload.dto.UserDTO;
import com.junaid.user_service.repository.UserRepository;
import com.junaid.user_service.service.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserDTO userDTO) {
        User user = new User();

        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());

        String plainPassword = userDTO.getPassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);

        user.setPassword(hashedPassword);
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public void deleteUser(Long userId) throws Exception {
        if(!userRepository.existsById(userId)){
            throw new Exception("User does not exist!");
        }
        userRepository.deleteById(userId);
    }
}
