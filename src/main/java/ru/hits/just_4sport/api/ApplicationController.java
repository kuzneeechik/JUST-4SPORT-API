package ru.hits.just_4sport.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.hits.just_4sport.model.api.team.TeamApplicationModel;
import ru.hits.just_4sport.service.ApplicationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/event/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/{id}/application")
    public ResponseEntity<Void> sendApplication(
            @PathVariable UUID id,
            @RequestBody TeamApplicationModel teamApplication
    ) {
        applicationService.sendApplication(id, teamApplication);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/application")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails user
    ) {
        applicationService.cancelApplication(id, user.getUsername());

        return ResponseEntity.ok().build();
    }
}
