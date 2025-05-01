package com.joeburin.event_photo_sharing.repository;

import com.joeburin.event_photo_sharing.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}