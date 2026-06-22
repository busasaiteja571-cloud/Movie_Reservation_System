package com.example.Movie_Reservation_System_BackEnd.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Movie_Reservation_System_BackEnd.entity.Movie;
import com.example.Movie_Reservation_System_BackEnd.exception.ResourceNotFoundException;
import com.example.Movie_Reservation_System_BackEnd.repository.MovieRepository;

@Service
public class MovieService {
    
    private final MovieRepository movieRepository;

    // Best Practice: Constructor Dependency Injection
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * EXPLANATION: Fetches all movie records from MySQL.
     * `readOnly = true` optimizes the database performance since Hibernate doesn't need to 
     * run dirty checking or track entities for updates during this transactional thread read lifecycle.
     */
    @Transactional(readOnly = true)
    public List<Movie> getAllMovies() { 
        return movieRepository.findAll(); 
    }

    /**
     * EXPLANATION: Saves a freshly generated movie configuration containing its binary BLOB data.
     */
    @Transactional
    public Movie addMovie(Movie movie) { 
        return movieRepository.save(movie); 
    }

    /**
     * EXPLANATION: Updates an existing movie record.
     * We safely fetch the database record first, swap out individual string/enum values, 
     * and apply the conditional safety guard clause below to handle the byte array image updates cleanly.
     */
    @Transactional
    public Movie updateMovie(Long id, Movie movieDetails) {
        // Find existing record or throw our customized 404 handler exception
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        
        // Map changes across to our managed entity
        movie.setTitle(movieDetails.getTitle());
        movie.setDescription(movieDetails.getDescription());
        movie.setGenre(movieDetails.getGenre());
        
        /* * FIX / EXPLANATION: 
         * 1. Swapped old '.setPosterImageUrl' out for our matching binary '.setPosterImage'.
         * 2. Added a conditional check. If the admin user updates a text parameter (like description) 
         * but chooses not to upload a brand new file, `movieDetails.getPosterImage()` will arrive empty. 
         * This check prevents us from accidentally clearing out the existing database poster.
         */
        if (movieDetails.getPosterImage() != null && movieDetails.getPosterImage().length > 0) {
            movie.setPosterImage(movieDetails.getPosterImage());
        }
        
        // Flush modifications into the physical table schema row
        return movieRepository.save(movie);
    }

    /**
     * EXPLANATION: Deletes a movie completely. If an ID is invalid, it fires our 404 handler immediately 
     * before running any delete operations, keeping relational maps healthy.
     */
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        
        movieRepository.delete(movie);
    }
}