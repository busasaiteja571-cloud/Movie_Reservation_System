package com.example.Movie_Reservation_System_BackEnd.controller;

import com.example.Movie_Reservation_System_BackEnd.entity.Movie;
import com.example.Movie_Reservation_System_BackEnd.entity.Genre;
import com.example.Movie_Reservation_System_BackEnd.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * 1. READ ALL MOVIES
     * URL: GET /api/movies
     */
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    /**
     * 2. CREATE A MOVIE WITH AN IMAGE FILE
     * URL: POST /api/movies
     * * EXPLANATION: 
     * - `consumes = MediaType.MULTIPART_FORM_DATA_VALUE`: This is critical. It forces the endpoint to 
     * accept form data, breaking down incoming text parameters and binary files simultaneously.
     * - `@RequestParam`: Since we are no longer parsing a singular unified JSON body via `@RequestBody`, 
     * we capture individual form keys sent by the frontend client application.
     * - `MultipartFile file`: Represents the raw uploaded data stream containing your image payload.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMovie(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("genre") Genre genre,
            @RequestParam("image") MultipartFile file) {
        try {
            // Rebuilding the domain model manually since we aren't auto-binding an entire JSON payload
            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setDescription(description);
            movie.setGenre(genre);
            
            // Convert file upload stream directly into binary bytes
            if (file != null && !file.isEmpty()) {
                movie.setPosterImage(file.getBytes());
            }

            Movie savedMovie = movieService.addMovie(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
        } catch (IOException e) {
            // Handled locally in case image reading encounters operating system hardware stream errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process image payload data.");
        }
    }

    /**
     * 3. UPDATE AN EXISTING MOVIE RECORD
     * URL: PUT /api/movies/{id}
     * * EXPLANATION: 
     * Form data is used here as well. `required = false` is added to the image parameter so that 
     * if an administrator only wants to update a typo in the description, they don't have to 
     * re-upload the image file every time.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMovie(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("genre") Genre genre,
            @RequestParam(value = "image", required = false) MultipartFile file) {
        try {
            Movie movieDetails = new Movie();
            movieDetails.setTitle(title);
            movieDetails.setDescription(description);
            movieDetails.setGenre(genre);

            if (file != null && !file.isEmpty()) {
                movieDetails.setPosterImage(file.getBytes());
            }

            Movie updatedMovie = movieService.updateMovie(id, movieDetails);
            return ResponseEntity.ok(updatedMovie);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update movie image payload data.");
        }
    }

    /**
     * 4. DELETE A MOVIE RECORD
     * URL: DELETE /api/movies/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Movie removed successfully.");
    }

    /**
     * 5. DIRECT BINARY STREAMING ENDPOINT
     * URL: GET /api/movies/{id}/image
     * * EXPLANATION: 
     * This method fetches the image data and serves it directly to the browser.
     * This allows a frontend application to display the image using a standard HTML image tag, like so:
     * <img src="http://localhost:8080/api/movies/5/image" />
     * * By setting the content type to `MediaType.IMAGE_JPEG`, the browser recognizes that this is an 
     * image to be rendered rather than plain text.
     */
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getMovieImage(@PathVariable Long id) {
        List<Movie> movies = movieService.getAllMovies();
        Movie targetMovie = movies.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));

        if (targetMovie.getPosterImage() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) 
                .body(targetMovie.getPosterImage());
    }
}