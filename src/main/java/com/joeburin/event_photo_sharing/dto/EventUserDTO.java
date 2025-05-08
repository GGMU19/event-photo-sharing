package com.joeburin.event_photo_sharing.dto;

public class EventUserDTO {
    private EventUserIdDTO id;
    private EventDTO event;
    private UserDTO user;
    private String role;

    // Getters and setters
    public EventUserIdDTO getId() { return id; }
    public void setId(EventUserIdDTO id) { this.id = id; }
    public EventDTO getEvent() { return event; }
    public void setEvent(EventDTO event) { this.event = event; }
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}