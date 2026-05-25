package ru.hits.just_4sport.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hits.just_4sport.model.api.user.UserProfileModel;
import ru.hits.just_4sport.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileModel> getUserProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserProfile(id));

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserProfile(
            @PathVariable UUID id,
            @RequestBody UserUpdateProfileModel userProfile
    ) {
        userProfileService.updateUserProfile(id, userProfile);

        return ResponseEntity.ok().build();
    }

    }
}
