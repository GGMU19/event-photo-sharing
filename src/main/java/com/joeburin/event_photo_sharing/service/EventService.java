package com.joeburin.event_photo_sharing.service;

import com.joeburin.event_photo_sharing.entity.Event;
import com.joeburin.event_photo_sharing.entity.User;
import com.joeburin.event_photo_sharing.repository.EventRepository;
import com.joeburin.event_photo_sharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Event createEvent(Event event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        System.out.println("Create Event -> Authenticated username: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Create Event -> User role: " + user.getRole());

        if (!user.getRole().equals("ROLE_ORGANIZER")) {
            throw new SecurityException("Only ORGANIZER can create events");
        }

        event.setOrganizerId(user.getId());
        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(Long eventId, Event updatedEvent) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        System.out.println("Update Event -> Authenticated username: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        System.out.println("Update Event -> User role: " + user.getRole());
        System.out.println("Update Event -> Event organizer ID: " + existingEvent.getOrganizerId());

        if (!existingEvent.getOrganizerId().equals(user.getId()) || !user.getRole().equals("ROLE_ORGANIZER")) {
            throw new SecurityException("Only the event's ORGANIZER can update it");
        }

        existingEvent.setName(updatedEvent.getName());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setDateTime(updatedEvent.getDateTime());
        return eventRepository.save(existingEvent);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        System.out.println("Delete Event -> Authenticated username: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        System.out.println("Delete Event -> User role: " + user.getRole());
        System.out.println("Delete Event -> Event organizer ID: " + event.getOrganizerId());

        if (!event.getOrganizerId().equals(user.getId()) || !user.getRole().equals("ROLE_ORGANIZER")) {
            throw new SecurityException("Only the event's ORGANIZER can delete it");
        }

        eventRepository.delete(event);
    }

    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Event> getEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }
}
