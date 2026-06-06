package ru.hits.just_4sport.api;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.just_4sport.service.PhotoService;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping("/{photoPath}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String photoPath) {
        var file = photoService.getPhoto(photoPath);

        return ResponseEntity.ok()
                .contentType(file.getMediaType())
                .body(file.getResource());
    }
}
