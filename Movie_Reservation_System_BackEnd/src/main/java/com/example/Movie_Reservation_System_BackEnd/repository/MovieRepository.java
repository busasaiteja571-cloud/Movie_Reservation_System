package com.example.Movie_Reservation_System_BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Movie_Reservation_System_BackEnd.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie , Long>{

}
