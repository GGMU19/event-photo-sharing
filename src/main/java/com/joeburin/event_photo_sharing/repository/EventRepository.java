package com.joeburin.event_photo_sharing.repository;

import com.joeburin.event_photo_sharing.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}