package com.joeburin.event_photo_sharing.dto;

import java.time.LocalDateTime;

public class CreateOrUpdateEventDTO {
    private String name;
    private String description;
    private LocalDateTime dateTime;

    public CreateOrUpdateEventDTO() {}

    public CreateOrUpdateEventDTO(String name, String description, LocalDateTime dateTime) {
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
}
