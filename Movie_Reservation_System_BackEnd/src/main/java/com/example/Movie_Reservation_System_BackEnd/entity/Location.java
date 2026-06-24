package com.example.Movie_Reservation_System_BackEnd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 80)
    private String state;

    @Size(max = 80)
    private String country;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, unique = true)
    private String slug;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Theater> theaters = new ArrayList<>();


    protected Location() {}

    public Location(String name, String state, String country, String slug) {
        this.name = name;
        this.state = state;
        this.country = country;
        this.slug = slug;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<Theater> getTheaters() {
        return theaters;
    }

    public void addTheater(Theater theater) {
        theaters.add(theater);
        theater.setLocation(this);
    }

    public void removeTheater(Theater theater) {
        theaters.remove(theater);
        theater.setLocation(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return id != null && id.equals(location.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
