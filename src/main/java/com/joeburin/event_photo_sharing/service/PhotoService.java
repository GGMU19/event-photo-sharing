package com.joeburin.event_photo_sharing.service;

import com.joeburin.event_photo_sharing.entity.Event;
import com.joeburin.event_photo_sharing.entity.Photo;
import com.joeburin.event_photo_sharing.entity.User;
import com.joeburin.event_photo_sharing.repository.EventRepository;
import com.joeburin.event_photo_sharing.repository.PhotoRepository;
import com.joeburin.event_photo_sharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3Client s3Client;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    @Transactional
    public Photo uploadPhoto(Long eventId, MultipartFile file) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String s3Key = "photos/" + fileName;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String s3Url = String.format("https://%s.s3.amazonaws.com/%s", bucketName, s3Key);
            Photo photo = new Photo(s3Url, event, user.getId(), LocalDateTime.now());
            return photoRepository.save(photo);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload photo to S3: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Page<Photo> getPhotosByEventIdPaginated(Long eventId, int page, int size) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("uploadedAt").descending());
        return photoRepository.findByEventId(eventId, pageRequest);
    }

    @Transactional
    public void deletePhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isUploader = photo.getUploaderId().equals(currentUser.getId());
        boolean isOrganizer = photo.getEvent().getOrganizer(userRepository).getId().equals(currentUser.getId());

        if (!isUploader && !isOrganizer) {
            throw new RuntimeException("You are not authorized to delete this photo.");
        }

        try {
            String s3Key = extractKeyFromS3Url(photo.getUrl());
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete photo from S3: " + e.getMessage());
        }

        photoRepository.delete(photo);
    }

    private String extractKeyFromS3Url(String s3Url) {
        return s3Url.substring(s3Url.indexOf(".com/") + 5);
    }
}
