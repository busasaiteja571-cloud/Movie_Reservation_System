package com.example.Movie_Reservation_System_BackEnd.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    // ==========================================
    // 1. CONSTRUCTORS
    // ==========================================

    // Default constructor for Jackson JSON deserialization
    public AuthRequest() {
        super();
    }

    // Fully-parameterized constructor
    public AuthRequest(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    // ==========================================
    // 2. GETTERS AND SETTERS
    // ==========================================

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}