package ru.hits.just_4sport.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.just_4sport.model.api.team.TeamAuthorModel;
import ru.hits.just_4sport.service.EventAuthorService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/author-events")
@RequiredArgsConstructor
public class EventAuthorController {

    private final EventAuthorService eventAuthorService;

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<TeamAuthorModel>> getParticipants(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(eventAuthorService.getParticipants(user.getUsername(), id));
    }
}
