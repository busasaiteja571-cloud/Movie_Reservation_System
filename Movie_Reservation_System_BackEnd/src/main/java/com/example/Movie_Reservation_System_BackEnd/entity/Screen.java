package com.example.Movie_Reservation_System_BackEnd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "screens")
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 80)
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @NotNull
    @Min(1)
    @Column(name = "seat_capacity", nullable = false)
    private Integer seatCapacity;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes = new ArrayList<>();


    protected Screen() {}

    public Screen(String name, Theater theater, Integer seatCapacity) {
        this.name = name;
        this.theater = theater;
        this.seatCapacity = seatCapacity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public Integer getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(Integer seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public void addShowtime(Showtime showtime) {
        showtimes.add(showtime);
        showtime.setScreen(this);
    }

    public void removeShowtime(Showtime showtime) {
        showtimes.remove(showtime);
        showtime.setScreen(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Screen)) return false;
        Screen screen = (Screen) o;
        return id != null && id.equals(screen.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
