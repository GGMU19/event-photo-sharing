package com.joeburin.event_photo_sharing.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventUserId implements Serializable {

    private Long event;
    private Long user;

    public EventUserId() {}

    public EventUserId(Long event, Long user) {
        this.event = event;
        this.user = user;
    }

    // Getters and setters
    public Long getEvent() { return event; }
    public void setEvent(Long event) { this.event = event; }

    public Long getUser() { return user; }
    public void setUser(Long user) { this.user = user; }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventUserId)) return false;
        EventUserId that = (EventUserId) o;
        return Objects.equals(event, that.event) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, user);
    }
}
