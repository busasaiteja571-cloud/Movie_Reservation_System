package com.example.Movie_Reservation_System_BackEnd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User association is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Showtime association is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @NotNull(message = "Number of seats to book is required")
    @Min(value = 1, message = "You must book at least 1 seat")
    @Column(name = "seats_booked", nullable = false)
    private Integer seatsBooked;

    @NotNull(message = "Booking time is required")
    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @NotNull
    @Column(name = "is_cancelled", nullable = false)
    private Boolean isCancelled = false;

    // ==========================================
    // 1. CONSTRUCTORS
    // ==========================================

    // Required by JPA: Hibernate uses this blank constructor to rebuild entities from MySQL records
    protected Reservation() {
        super();
    }

    // Business Constructor: Used to cleanly initialize new reservations without passing a manual ID
    public Reservation(User user, Showtime showtime, Integer seatsBooked) {
        super();
        this.user = user;
        this.showtime = showtime;
        this.seatsBooked = seatsBooked;
        this.bookingTime = LocalDateTime.now(); // Automatically sets booking timestamp on creation
        this.isCancelled = false;               // New reservations default to active
    }

    // ==========================================
    // 2. GETTERS AND SETTERS
    // ==========================================

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public Integer getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(Integer seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    // ==========================================
    // 3. JPA IDENTIFIER METHODS
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation other = (Reservation) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}