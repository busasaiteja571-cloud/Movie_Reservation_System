package com.example.Movie_Reservation_System_BackEnd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Movie_Reservation_System_BackEnd.entity.Reservation;
import com.example.Movie_Reservation_System_BackEnd.entity.Showtime;
import com.example.Movie_Reservation_System_BackEnd.entity.User;
import com.example.Movie_Reservation_System_BackEnd.exception.ResourceNotFoundException;
import com.example.Movie_Reservation_System_BackEnd.repository.ReservationRepository;
import com.example.Movie_Reservation_System_BackEnd.repository.ShowtimeRepository;

@Service
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final ShowtimeRepository showtimeRepository;

    // Modern Constructor-Based Dependency Injection
    public ReservationService(ReservationRepository reservationRepository, ShowtimeRepository showtimeRepository) {
        this.reservationRepository = reservationRepository;
        this.showtimeRepository = showtimeRepository;
    }

    @Transactional
    public Reservation reserveSeats(User user, Long showtimeId, Integer seatsRequested) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + showtimeId));

        if (showtime.getAvailableSeats() < seatsRequested) {
            throw new IllegalArgumentException("Not enough seats available! Available: " + showtime.getAvailableSeats());
        }

        // 1. Deduct inventory capacity cleanly
        showtime.setAvailableSeats(showtime.getAvailableSeats() - seatsRequested);
        showtimeRepository.save(showtime);

        // 2. FIXED: Uses your updated business constructor signature
        Reservation reservation = new Reservation(user, showtime, seatsRequested);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void cancelReservation(Long reservationId, User user) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

        // Validation Checks
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Unauthorized cancellation attempt.");
        }
        if (reservation.getIsCancelled()) {
            throw new IllegalArgumentException("Reservation is already cancelled.");
        }
        if (reservation.getShowtime().getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot cancel past showtime reservations.");
        }

        // Alter states
        reservation.setIsCancelled(true);
        Showtime showtime = reservation.getShowtime();
        showtime.setAvailableSeats(showtime.getAvailableSeats() + reservation.getSeatsBooked());
        
        showtimeRepository.save(showtime);
        reservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getAdminReport() {
        // IMPROVED: Offloads heavy computation completely to MySQL instead of running loops in Java memory
        List<Reservation> activeReservations = reservationRepository.findAllActiveReservationsWithDetails();
        Long totalSeatsBooked = reservationRepository.countTotalActiveSeatsBooked();
        Double totalRevenue = reservationRepository.calculateTotalGrossRevenue();

        return Map.of(
            "totalActiveReservations", activeReservations.size(),
            "totalSeatsBooked", totalSeatsBooked,
            "totalRevenue", totalRevenue
        );
    }
}