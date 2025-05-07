package com.joeburin.event_photo_sharing.controller;

import com.joeburin.event_photo_sharing.dto.PhotoDTO;
import com.joeburin.event_photo_sharing.entity.Photo;
import com.joeburin.event_photo_sharing.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping
    public ResponseEntity<PhotoDTO> uploadPhoto(@RequestParam("eventId") Long eventId,
                                                @RequestParam("file") MultipartFile file) {
        Photo photo = photoService.uploadPhoto(eventId, file);
        return ResponseEntity.ok(PhotoDTO.fromEntity(photo));
    }

    @GetMapping
    public ResponseEntity<List<PhotoDTO>> getPhotosByEventId(@RequestParam("eventId") Long eventId) {
        List<PhotoDTO> photos = photoService.getPhotosByEventId(eventId)
                .stream()
                .map(PhotoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(photos);
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
        photoService.deletePhoto(photoId);
        return ResponseEntity.noContent().build();
    }

}