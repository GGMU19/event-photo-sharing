package com.joeburin.event_photo_sharing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "event_users")
public class EventUser {

    @EmbeddedId
    private EventUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("event")  // This maps the event part of the composite key
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user")  // This maps the user part of the composite key
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String role;

    public EventUser() {}

    public EventUser(Event event, User user, String role) {
        this.id = new EventUserId(event.getId(), user.getId());  // Set the composite ID
        this.event = event;
        this.user = user;
        this.role = role;
    }

    // Getters and setters
    public EventUserId getId() { return id; }
    public Event getEvent() { return event; }
    public User getUser() { return user; }
    public String getRole() { return role; }

    public void setId(EventUserId id) { this.id = id; }
    public void setEvent(Event event) { this.event = event; }
    public void setUser(User user) { this.user = user; }
    public void setRole(String role) { this.role = role; }
}
