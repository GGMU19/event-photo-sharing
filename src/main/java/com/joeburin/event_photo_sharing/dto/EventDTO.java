package com.joeburin.event_photo_sharing.dto;

import com.joeburin.event_photo_sharing.entity.Event;

import java.time.LocalDateTime;

public class EventDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private Long organizerId;

    public EventDTO() {}

    public EventDTO(Long id, String name, String description, LocalDateTime dateTime, Long organizerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.organizerId = organizerId;
    }

    // Static factory method for mapping
    public static EventDTO fromEntity(Event event) {
        return new EventDTO(event.getId(), event.getName(), event.getDescription(), event.getDateTime(), event.getOrganizerId());
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public Long getOrganizerId() { return organizerId; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }
}