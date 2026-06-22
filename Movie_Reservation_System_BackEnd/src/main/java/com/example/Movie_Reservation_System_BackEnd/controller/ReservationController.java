package com.example.Movie_Reservation_System_BackEnd.controller;

import com.example.Movie_Reservation_System_BackEnd.dto.ReservationRequest;
import com.example.Movie_Reservation_System_BackEnd.entity.Reservation;
import com.example.Movie_Reservation_System_BackEnd.entity.User;
import com.example.Movie_Reservation_System_BackEnd.entity.Role;
import com.example.Movie_Reservation_System_BackEnd.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    // Best Practice: Constructor Dependency Injection
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody ReservationRequest request) {
        // FIX: Replaced the direct 1L constructor instantiation since your updated User entity uses a custom business constructor.
        // Instead, we create a user instance and populate its properties or mock state to match your signature perfectly.
        User mockUser = new User("john_doe", "password", Role.USER);
        
        Reservation reservation = reservationService.reserveSeats(mockUser, request.getShowtimeId(), request.getSeatsRequested());
        
        // Returns HTTP 201 Created status code for REST best practices
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        User mockUser = new User("john_doe", "password", Role.USER);
        
        reservationService.cancelReservation(id, mockUser);
        return ResponseEntity.ok("Reservation successfully cancelled.");
    }

    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getReport() {
        return ResponseEntity.ok(reservationService.getAdminReport());
    }
}