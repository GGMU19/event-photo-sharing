package com.joeburin.event_photo_sharing.service;

import com.joeburin.event_photo_sharing.entity.*;
import com.joeburin.event_photo_sharing.repository.EventUserRepository;
import com.joeburin.event_photo_sharing.repository.UserRepository;
import com.joeburin.event_photo_sharing.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventUserService {

    @Autowired
    private EventUserRepository eventUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    public boolean isUserAttendee(Long eventId, Long userId) {
        EventUserId eventUserId = new EventUserId(eventId, userId);
        return eventUserRepository.findById(eventUserId)
                .map(eventUser -> "attendee".equalsIgnoreCase(eventUser.getRole()))
                .orElse(false);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void addUserToEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EventUserId id = new EventUserId(eventId, userId);
        if (eventUserRepository.existsById(id)) {
            throw new RuntimeException("User already registered for this event");
        }

        EventUser eventUser = new EventUser(event, user, "attendee");
        eventUserRepository.save(eventUser);
    }

    public List<User> getAttendeesForEvent(Long eventId) {
        return eventUserRepository.findAttendeesByEventId(eventId).stream()
                .map(EventUser::getUser)
                .collect(Collectors.toList());
    }

    public boolean isUserRelatedToEvent(Long eventId, Long userId) {
        return eventUserRepository.findById(new EventUserId(eventId, userId)).isPresent();
    }
}