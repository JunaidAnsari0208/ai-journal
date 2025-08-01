package com.junaid.user_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marks this as a Spring component, so it can be injected.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Ensures the filter runs only once per request.

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // Our bean from ApplicationConfig

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Step 1. Check for the JWT in the "Authorization" header.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // If the header is missing or doesn't start with "Bearer ", pass the request on.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Pass to the next filter.
            return;
        }

        // Step 2. Extract the token from the header string.
        jwt = authHeader.substring(7); // "Bearer ".length() is 7

        // Step 3. Extract the username from the token using our JwtService.
        username = jwtService.extractUsername(jwt);

        // Step 4. If we have a username AND the user is not already authenticated...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load the user details from the database using our UserDetailsService.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Step 5. Check if the token is valid for this user.
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // If valid, create an authentication token...
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // We don't have credentials when creating from a token.
                        userDetails.getAuthorities()
                );
                // ...and set the details from the request.
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Step 6. Update the SecurityContextHolder. This is how we tell Spring Security the user is authenticated.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 7. Always pass the request to the next filter in the chain.
        filterChain.doFilter(request, response);
    }
}