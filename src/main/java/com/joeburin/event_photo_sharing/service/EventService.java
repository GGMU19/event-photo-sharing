package com.joeburin.event_photo_sharing.service;

import com.joeburin.event_photo_sharing.dto.EventUserDTO;
import com.joeburin.event_photo_sharing.dto.EventUserIdDTO;
import com.joeburin.event_photo_sharing.dto.EventDTO;
import com.joeburin.event_photo_sharing.dto.UserDTO;
import com.joeburin.event_photo_sharing.entity.Event;
import com.joeburin.event_photo_sharing.entity.EventUser;
import com.joeburin.event_photo_sharing.entity.EventUserId;
import com.joeburin.event_photo_sharing.entity.User;
import com.joeburin.event_photo_sharing.repository.EventRepository;
import com.joeburin.event_photo_sharing.repository.EventUserRepository;
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

    @Autowired
    private EventUserRepository eventUserRepository;

    @Transactional
    public Event createEvent(Event event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

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

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

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

    @Transactional
    public boolean isUserAttendee(Long eventId, Long userId) {
        return eventUserRepository.existsByEventIdAndUserIdAndRole(eventId, userId, "attendee");
    }

    @Transactional
    public void addUserToEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (eventUserRepository.existsByEventIdAndUserIdAndRole(eventId, userId, "attendee")) {
            throw new RuntimeException("User is already attending this event.");
        }

        EventUser eventUser = new EventUser(event, user, "attendee");
        eventUserRepository.save(eventUser);
    }

    @Transactional
    public EventUserDTO addAttendee(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<EventUser> existing = eventUserRepository.findById(new EventUserId(eventId, user.getId()));
        if (existing.isPresent()) {
            throw new IllegalArgumentException("User is already an attendee of this event");
        }

        EventUser eventUser = new EventUser(event, user, "attendee");
        EventUser saved = eventUserRepository.save(eventUser);

        // Convert to DTO
        EventUserDTO dto = new EventUserDTO();
        EventUserIdDTO idDto = new EventUserIdDTO();
        idDto.setEvent(eventId);
        idDto.setUser(user.getId());
        dto.setId(idDto);

        EventDTO eventDto = new EventDTO();
        eventDto.setId(event.getId());
        eventDto.setName(event.getName());
        eventDto.setDescription(event.getDescription());
        eventDto.setDateTime(event.getDateTime());
        eventDto.setOrganizerId(event.getOrganizerId());
        dto.setEvent(eventDto);

        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole());
        dto.setUser(userDto);

        dto.setRole(saved.getRole());

        return dto;
    }
}