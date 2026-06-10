package ru.hits.just_4sport.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.hits.just_4sport.model.api.team.TeamAuthorModel;
import ru.hits.just_4sport.model.api.team.TeamModel;
import ru.hits.just_4sport.service.EventParticipantsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class EventParticipantsController {

    private final EventParticipantsService eventParticipantsService;

    @GetMapping("/{eventId}")
    public ResponseEntity<List<TeamModel>> getParticipants(@PathVariable UUID eventId) {
        return ResponseEntity.ok(eventParticipantsService.getParticipantsForUser(eventId));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<TeamAuthorModel>> getParticipants(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID eventId
    ) {
        return ResponseEntity.ok(eventParticipantsService.getParticipantsForAuthor(user.getUsername(), eventId));
    }

    @DeleteMapping("/{eventId}/{teamId}")
    public ResponseEntity<Void> deleteParticipant(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID eventId,
            @PathVariable UUID teamId
    ) {
        eventParticipantsService.deleteParticipant(user.getUsername(), eventId, teamId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{eventId}/close")
    public ResponseEntity<Void> closeRecruitment(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID eventId
    ) {
        eventParticipantsService.closeRecruitment(user.getUsername(), eventId);

        return ResponseEntity.ok().build();
    }
}
