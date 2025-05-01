package com.joeburin.event_photo_sharing.repository;

import com.joeburin.event_photo_sharing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}