package com.joeburin.event_photo_sharing.repository;

import com.joeburin.event_photo_sharing.entity.EventUser;
import com.joeburin.event_photo_sharing.entity.EventUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventUserRepository extends JpaRepository<EventUser, EventUserId> {

    boolean existsByEventIdAndUserIdAndRole(Long eventId, Long userId, String role);

    @Query("SELECT eu FROM EventUser eu JOIN FETCH eu.user WHERE eu.event.id = :eventId AND LOWER(eu.role) = 'attendee'")
    List<EventUser> findAttendeesByEventId(@Param("eventId") Long eventId);
}