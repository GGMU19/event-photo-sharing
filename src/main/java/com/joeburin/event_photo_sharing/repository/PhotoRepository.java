package com.joeburin.event_photo_sharing.repository;

import com.joeburin.event_photo_sharing.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Page<Photo> findByEventId(Long eventId, Pageable pageable);
}