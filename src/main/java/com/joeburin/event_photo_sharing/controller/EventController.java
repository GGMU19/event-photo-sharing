package com.joeburin.event_photo_sharing.controller;

import com.joeburin.event_photo_sharing.dto.CreateOrUpdateEventDTO;
import com.joeburin.event_photo_sharing.dto.EventUserDTO;
import com.joeburin.event_photo_sharing.dto.EventDTO;
import com.joeburin.event_photo_sharing.entity.Event;
import com.joeburin.event_photo_sharing.entity.User;
import com.joeburin.event_photo_sharing.service.EventService;
import com.joeburin.event_photo_sharing.service.EventUserService;  // Assuming you have this service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventUserService eventUserService; // Service to check attendance status

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ORGANIZER')")
    public ResponseEntity<EventDTO> createEvent(@RequestBody CreateOrUpdateEventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setDateTime(eventDTO.getDateTime());

        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.ok(EventDTO.fromEntity(createdEvent));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ORGANIZER')")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody CreateOrUpdateEventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setDateTime(eventDTO.getDateTime());

        Event updatedEvent = eventService.updateEvent(id, event);
        return ResponseEntity.ok(EventDTO.fromEntity(updatedEvent));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ORGANIZER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        // Get the logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = eventUserService.findUserByUsername(username); // Fetch user by username

        // Fetch only the events where the user is an attendee
        List<EventDTO> eventDTOs = eventService.getAllEvents().stream()
                .filter(event -> eventUserService.isUserAttendee(event.getId(), user.getId())) // Check if user is an attendee
                .map(EventDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(eventDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        // Get the logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = eventUserService.findUserByUsername(username); // Fetch user by username

        // Check if the user is an attendee of the event
        if (!eventUserService.isUserAttendee(id, user.getId())) {
            return ResponseEntity.status(403).body(null);  // Forbidden if not an attendee
        }

        Optional<Event> eventOpt = eventService.getEventById(id);
        return eventOpt.map(EventDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{eventId}/attend")
    public ResponseEntity<Void> addUserToEvent(@PathVariable Long eventId, @RequestParam Long userId) {
        try {
            eventService.addUserToEvent(eventId, userId); // Add user to event
            return ResponseEntity.status(201).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null); // Bad Request if user is already attending
        }
    }

    @PostMapping("/{eventId}/attendees")
    @PreAuthorize("hasAuthority('ROLE_ORGANIZER')")
    public ResponseEntity<EventUserDTO> addAttendee(@PathVariable Long eventId, @RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            EventUserDTO eventUser = eventService.addAttendee(eventId, username);
            return ResponseEntity.ok(eventUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}/attendees")
    public ResponseEntity<List<String>> getEventAttendees(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = eventUserService.findUserByUsername(username);

        if (!eventUserService.isUserRelatedToEvent(id, user.getId())) {
            return ResponseEntity.status(403).build(); // Not an attendee or organizer
        }

        List<User> attendees = eventUserService.getAttendeesForEvent(id);
        List<String> attendeeUsernames = attendees.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        return ResponseEntity.ok(attendeeUsernames);
    }
}
