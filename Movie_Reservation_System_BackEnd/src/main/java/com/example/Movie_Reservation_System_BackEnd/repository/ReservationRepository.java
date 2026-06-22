package com.example.Movie_Reservation_System_BackEnd.repository;

import com.example.Movie_Reservation_System_BackEnd.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Optimized user history lookup that pulls full ticket data in one query
    @Query("SELECT r FROM Reservation r JOIN FETCH r.showtime s JOIN FETCH s.movie WHERE r.user.id = :userId ORDER BY r.bookingTime DESC")
    List<Reservation> findByUserIdWithDetails(@Param("userId") Long userId);
	
    // Fetches active bookings cleanly for dashboard reports
    @Query("SELECT r FROM Reservation r JOIN FETCH r.user JOIN FETCH r.showtime s JOIN FETCH s.movie WHERE r.isCancelled = false")
    List<Reservation> findAllActiveReservationsWithDetails();

    // Fast native counting for total physical tickets sold
    @Query("SELECT COALESCE(SUM(r.seatsBooked), 0) FROM Reservation r WHERE r.isCancelled = false")
    Long countTotalActiveSeatsBooked();

    // Calculates total gross revenue natively on the database engine
    @Query("SELECT COALESCE(SUM(r.seatsBooked * s.ticketPrice), 0.0) FROM Reservation r JOIN r.showtime s WHERE r.isCancelled = false")
    Double calculateTotalGrossRevenue();
}