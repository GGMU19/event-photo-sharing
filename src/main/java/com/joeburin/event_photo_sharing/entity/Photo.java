package com.joeburin.event_photo_sharing.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "s3_url", nullable = false)
    private String s3Url; // URL of the photo in AWS S3

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "uploader_id", nullable = false)
    private Long uploaderId; // Foreign key to users table

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    // Constructors
    public Photo() {}

    public Photo(String s3Url, Event event, Long uploaderId, LocalDateTime uploadedAt) {
        this.s3Url = s3Url;
        this.event = event;
        this.uploaderId = uploaderId;
        this.uploadedAt = uploadedAt;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getS3Url() { return s3Url; }
    public void setS3Url(String s3Url) { this.s3Url = s3Url; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public Long getUploaderId() { return uploaderId; }
    public void setUploaderId(Long uploaderId) { this.uploaderId = uploaderId; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    // Add getUrl() method to get the S3 URL
    public String getUrl() {
        return this.s3Url;
    }
}
