package ru.hits.just_4sport.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.hits.just_4sport.model.api.event.EventEditModel;
import ru.hits.just_4sport.service.EventAuthorService;

import java.util.UUID;

@RestController
@RequestMapping("/api/author-events")
@RequiredArgsConstructor
public class EventAuthorController {

    private final EventAuthorService eventAuthorService;

    @PutMapping("/{id}")
    public ResponseEntity<Void> editEvent(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id,
            @RequestBody EventEditModel eventData
    ) {
        eventAuthorService.editEvent(user.getUsername(), id, eventData);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id
    ) {
        eventAuthorService.deleteEvent(user.getUsername(), id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelEvent(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id
    ) {
        eventAuthorService.cancelEvent(user.getUsername(), id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<Void> finishEvent(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id
    ) {
        eventAuthorService.finishEvent(user.getUsername(), id);

        return ResponseEntity.ok().build();
    }
}
