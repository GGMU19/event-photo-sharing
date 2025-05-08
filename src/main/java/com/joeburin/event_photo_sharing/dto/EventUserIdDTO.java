package com.joeburin.event_photo_sharing.dto;

public class EventUserIdDTO {
    private Long event;
    private Long user;

    // Getters and setters
    public Long getEvent() { return event; }
    public void setEvent(Long event) { this.event = event; }
    public Long getUser() { return user; }
    public void setUser(Long user) { this.user = user; }
}