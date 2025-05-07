package com.joeburin.event_photo_sharing.controller;

import com.joeburin.event_photo_sharing.dto.PhotoDTO;
import com.joeburin.event_photo_sharing.entity.Photo;
import com.joeburin.event_photo_sharing.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/paginated")
    public ResponseEntity<Page<PhotoDTO>> getPhotosByEventPaginated(
            @RequestParam("eventId") Long eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Photo> photoPage = photoService.getPhotosByEventIdPaginated(eventId, page, size);
        Page<PhotoDTO> dtoPage = photoPage.map(PhotoDTO::fromEntity);
        return ResponseEntity.ok(dtoPage);
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
        photoService.deletePhoto(photoId);
        return ResponseEntity.noContent().build();
    }
}
