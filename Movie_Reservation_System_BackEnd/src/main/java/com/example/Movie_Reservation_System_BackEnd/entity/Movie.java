package com.example.Movie_Reservation_System_BackEnd.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob; 
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "movies")
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Movie title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    @Column(nullable = false, length = 255)
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    /**
     * EXPLANATION: @Lob (Large Object) tells Hibernate that this field will hold heavy binary data.
     * In MySQL, this automatically translates into a `LONGBLOB` column type. 
     * We specify a length of 10,000,000 bytes to accommodate file uploads up to roughly 10 megabytes.
     */
    @Lob 
    @Column(name = "poster_image", length = 10000000) 
    private byte[] posterImage;
    
    @NotNull(message = "Genre is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Genre genre;
    
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes = new ArrayList<>();
    
    // ==========================================
    // 1. CONSTRUCTORS
    // ==========================================

    // Required by JPA: Used by Hibernate to instantiate objects via reflection when querying database rows.
    protected Movie() {
        super();
    }

    // Business Constructor: Used to cleanly initialize new movies programmatically.
    public Movie(String title, String description, byte[] posterImage, Genre genre) {
        super();
        this.title = title;
        this.description = description;
        this.posterImage = posterImage;
        this.genre = genre;
    }

    // ==========================================
    // 2. RELATIONSHIP HELPER METHODS
    // ==========================================

    public void addShowtime(Showtime showtime) {
        showtimes.add(showtime);
        showtime.setMovie(this);
    }

    public void removeShowtime(Showtime showtime) {
        showtimes.remove(showtime);
        showtime.setMovie(null);
    }

    // ==========================================
    // 3. GETTERS AND SETTERS
    // ==========================================

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // EXPLANATION: Returns the raw byte array data. When converted to JSON, 
    // Spring Boot automatically encodes this into a Base64 string for safe text transit.
    public byte[] getPosterImage() { return posterImage; }
    public void setPosterImage(byte[] posterImage) { this.posterImage = posterImage; }

    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }

    public List<Showtime> getShowtimes() { return showtimes; }
    public void setShowtimes(List<Showtime> showtimes) { this.showtimes = showtimes; }

    // ==========================================
    // 4. OPTIMIZED IDENTITY METHODS
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return id != null && id.equals(movie.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); 
    }
}