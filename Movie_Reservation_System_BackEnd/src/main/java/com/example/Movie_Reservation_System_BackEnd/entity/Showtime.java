package com.example.Movie_Reservation_System_BackEnd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "showtimes")
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RELATIONSHIP: Many showtimes belong to one Movie
    @NotNull(message = "Movie association is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    // RULE: Showtime must be a date/time in the future
    @NotNull(message = "Start time is required")
    @Future(message = "Showtime must be scheduled in the future")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    // RULE: Capacity cannot be negative, minimum 1 seat
    @NotNull(message = "Total capacity is required")
    @Min(value = 1, message = "Total capacity must be at least 1")
    @Column(name = "total_capacity", nullable = false)
    private Integer totalCapacity;

    // RULE: Available seats cannot be negative
    @NotNull(message = "Available seats count is required")
    @Min(value = 0, message = "Available seats cannot be negative")
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    // RULE: Price cannot be negative
    @NotNull(message = "Ticket price is required")
    @Min(value = 0, message = "Ticket price cannot be negative")
    @Column(name = "ticket_price", nullable = false)
    private Double ticketPrice;

    // Optimistic locking version control to prevent booking race conditions
    @Version
    private Long version;

    // ==========================================
    // 1. CONSTRUCTORS
    // ==========================================

    // Required by JPA
    protected Showtime() {
        super();
    }

    // Recommended Business Constructor
    public Showtime(Movie movie, LocalDateTime startTime, Integer totalCapacity, Double ticketPrice) {
        super();
        this.movie = movie;
        this.startTime = startTime;
        this.totalCapacity = totalCapacity;
        this.availableSeats = totalCapacity; // Initially, all seats are available
        this.ticketPrice = ticketPrice;
    }

    // ==========================================
    // 2. GETTERS AND SETTERS
    // ==========================================

    public Long getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Long getVersion() {
        return version;
    }

    // ==========================================
    // 3. JPA IDENTIFIER METHODS
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Showtime)) return false;
        Showtime showtime = (Showtime) o;
        return id != null && id.equals(showtime.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}