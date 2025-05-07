package com.joeburin.event_photo_sharing.dto;

import com.joeburin.event_photo_sharing.entity.Photo;

import java.time.LocalDateTime;

public class PhotoDTO {
    private Long id;
    private String s3Url;
    private Long eventId;
    private Long uploaderId;
    private LocalDateTime uploadedAt;

    // Constructor
    public PhotoDTO(Long id, String s3Url, Long eventId, Long uploaderId, LocalDateTime uploadedAt) {
        this.id = id;
        this.s3Url = s3Url;
        this.eventId = eventId;
        this.uploaderId = uploaderId;
        this.uploadedAt = uploadedAt;
    }

    // Static factory method for mapping
    public static PhotoDTO fromEntity(Photo photo) {
        return new PhotoDTO(
                photo.getId(),
                photo.getS3Url(),
                photo.getEvent().getId(), // Extract eventId from Event
                photo.getUploaderId(), // Using uploaderId as Long
                photo.getUploadedAt()
        );
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
