package com.example.Movie_Reservation_System_BackEnd.repository;

import com.example.Movie_Reservation_System_BackEnd.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    
    // Custom query to fetch showtimes that fall within a precise date range
    @Query("SELECT s FROM Showtime s JOIN FETCH s.movie WHERE s.startTime BETWEEN :start AND :end")
    List<Showtime> findShowtimesByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}