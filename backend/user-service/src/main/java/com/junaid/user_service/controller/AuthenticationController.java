package com.junaid.user_service.controller;

import com.junaid.user_service.model.User;
import com.junaid.user_service.payload.dto.AuthenticationRequest;
import com.junaid.user_service.payload.dto.AuthenticationResponse;
import com.junaid.user_service.payload.dto.RefreshTokenRequest;
import com.junaid.user_service.payload.dto.UserDTO;
import com.junaid.user_service.security.JwtService;
import com.junaid.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // Base path for all authentication endpoints
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Endpoint for user registration.
     * It's public because of the rules we set in SecurityConfig.
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDTO request) {
        User registeredUser = userService.registerUser(request);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    /**
     * Endpoint for user login.
     * It takes username and password, and if they are correct, returns an access and refresh token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        // 1. Authenticate the user using the manager. Spring Security handles the password check.
        // If credentials are bad, it will throw an exception.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. If authentication is successful, find the user.
        final UserDetails user = userService.findByUsername(request.getUsername()).orElseThrow();

        // 3. Generate the tokens.
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // 4. Build the response object and send it back.
        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to get a new access token using a refresh token.
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);

        UserDetails user = userService.findByUsername(username).orElseThrow();

        // Validate the refresh token
        if (jwtService.isTokenValid(refreshToken, user)) {
            // Generate a new access token
            String newAccessToken = jwtService.generateToken(user);

            // We can return the same refresh token or a new one. Here we return the same one.
            AuthenticationResponse response = AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();
            return ResponseEntity.ok(response);
        } else {
            // If the refresh token is invalid or expired
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
