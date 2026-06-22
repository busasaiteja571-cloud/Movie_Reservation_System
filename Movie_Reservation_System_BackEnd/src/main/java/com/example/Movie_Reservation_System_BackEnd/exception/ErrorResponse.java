package com.example.Movie_Reservation_System_BackEnd.exception;

import java.util.Date;

public class ErrorResponse {
    
    private Date timestamp;
    private String message;
    private String details;

    // ==========================================
    // 1. CONSTRUCTORS
    // ==========================================

    // Default constructor for JSON mapping frameworks
    public ErrorResponse() {
        super();
    }

    // Fully-parameterized constructor to easily generate error payloads
    public ErrorResponse(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    // ==========================================
    // 2. GETTERS AND SETTERS
    // ==========================================

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}