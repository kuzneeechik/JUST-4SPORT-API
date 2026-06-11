package ru.hits.just_4sport.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hits.just_4sport.model.api.user.UserProfileModel;
import ru.hits.just_4sport.model.api.user.UserUpdateProfileModel;
import ru.hits.just_4sport.service.UserProfileService;

import java.util.UUID;

@RestController
@RequestMapping("api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<UserProfileModel> getMyProfile(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(userProfileService.getMyProfile(user.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileModel> getUserProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(userProfileService.getUserProfile(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserProfile(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateProfileModel userProfile
    ) {
        userProfileService.updateUserProfile(id, userProfile);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable UUID id) {
        userProfileService.deleteUserProfile(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping(
            value = "/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> setUserProfilePhoto(
            @AuthenticationPrincipal UserDetails user,
            @RequestPart("file") MultipartFile photo
    ) {
        userProfileService.setUserProfilePhoto(user.getUsername(), photo);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/photo")
    public ResponseEntity<Void> deleteUserProfilePhoto(
            @AuthenticationPrincipal UserDetails user
    ) {
        userProfileService.deleteUserProfilePhoto(user.getUsername());

        return ResponseEntity.ok().build();
    }
}
