package com.junaid.user_service.config;

import com.junaid.user_service.model.Role;
import com.junaid.user_service.model.User;
import com.junaid.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * This method will be executed upon application startup.
     */
    @Override
    public void run(String... args) throws Exception {
        // Check if an admin user already exists to avoid creating duplicates
        if (userRepository.findByUserName("admin").isEmpty()) {
            System.out.println("No admin account found. Creating one...");

            // Create the admin user object
            User adminUser = User.builder()
                    .userName("admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("adminpass")) // Use a strong password in a real app
                    .role(Role.ADMIN)
                    .build();

            // Save the admin user to the database
            userRepository.save(adminUser);

            System.out.println("Admin account created successfully.");
        } else {
            System.out.println("Admin account already exists.");
        }
    }
}