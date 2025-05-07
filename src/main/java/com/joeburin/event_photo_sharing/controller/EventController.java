package com.joeburin.event_photo_sharing.controller;

import com.joeburin.event_photo_sharing.dto.CreateOrUpdateEventDTO;
import com.joeburin.event_photo_sharing.dto.EventDTO;
import com.joeburin.event_photo_sharing.entity.Event;
import com.joeburin.event_photo_sharing.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

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
        List<EventDTO> eventDTOs = eventService.getAllEvents().stream()
                .map(EventDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        return eventOpt.map(EventDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
