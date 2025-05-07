package com.joeburin.event_photo_sharing.entity;

import com.joeburin.event_photo_sharing.repository.UserRepository;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "organizer_id", nullable = false)
    private Long organizerId;

    @ManyToMany(mappedBy = "events")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Photo> photos = new HashSet<>();

    // Constructors
    public Event() {
    }

    public Event(String name, String description, LocalDateTime dateTime, Long organizerId) {
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.organizerId = organizerId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    // Add getOrganizer() method to get the organizer User object
    @Transient
    public User getOrganizer(UserRepository userRepository) {
        return userRepository.findById(organizerId)
                .orElseThrow(() -> new RuntimeException("Organizer not found"));
    }
}
