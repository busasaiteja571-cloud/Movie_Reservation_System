package com.example.Movie_Reservation_System_BackEnd.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    // Constructor-Based Dependency Injection
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. Quick Exit: If no Authorization header exists or isn't a Bearer token, hand off to next filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract token string (skipping "Bearer " prefix which takes up exactly 7 characters)
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        // 3. Process Validation if a username exists and security context is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            if (jwtService.isTokenValid(jwt, username)) {
                // Pull out custom user role clearances stored in the payload claims
                String role = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));
                
                // Spring Security requires "ROLE_" prefixed to roles for hasRole("ADMIN") matchers to succeed
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                );
                
                // Track source web details (IP address, session details) inside token context
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Save authenticated token context instance into global app thread safety window
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 4. Advance execution control cleanly onward down the servlet lifecycle line
        filterChain.doFilter(request, response);
    }
}