package com.example.Movie_Reservation_System_BackEnd.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReservationRequest {

    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    @NotNull(message = "Number of seats requested is required")
    @Min(value = 1, message = "You must request at least 1 seat")
    private Integer seatsRequested;

    // ==========================================
    // 1. CONSTRUCTORS
    // ==========================================

    // Default No-Args Constructor required for JSON Deserialization (Jackson)
    public ReservationRequest() {
        super();
    }

    // Parameterized Constructor for easy object instantiation in tests/services
    public ReservationRequest(Long showtimeId, Integer seatsRequested) {
        super();
        this.showtimeId = showtimeId;
        this.seatsRequested = seatsRequested;
    }

    // ==========================================
    // 2. GETTERS AND SETTERS
    // ==========================================

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public Integer getSeatsRequested() {
        return seatsRequested;
    }

    public void setSeatsRequested(Integer seatsRequested) {
        this.seatsRequested = seatsRequested;
    }
}