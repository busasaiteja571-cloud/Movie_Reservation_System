package com.example.Movie_Reservation_System_BackEnd.entity;

import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users") // Good practice: keep table names lowercase in MySQL
public class User {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @NotBlank(message = "Password is required")
    @Column(nullable = false, length = 60) 
    private String password;
    
    @NotNull(message = "User role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    // ==========================================
    // 1. CONSTRUCTORS
    // ==========================================

    // Required by JPA: Hibernate needs this blank constructor to load data from MySQL
    public User() {
        super();
    }

    // Business Constructor: Used in your code to create new users without passing a manual ID
    public User(String username, String password, Role role) {
        super();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ==========================================
    // 2. GETTERS AND SETTERS
    // ==========================================

    public Long getId() {
        return id;
    }

    // Note: Kept empty/removed setId() to let MySQL securely handle primary keys

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // ==========================================
    // 3. JPA BEST PRACTICE METHODS
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User other = (User) o;
        return id != null && id.equals(other.id); // Securely matches via primary key
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); 
    }
}