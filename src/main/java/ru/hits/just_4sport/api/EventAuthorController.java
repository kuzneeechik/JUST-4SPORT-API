package ru.hits.just_4sport.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hits.just_4sport.model.api.event.EventEditModel;
import ru.hits.just_4sport.model.api.game.GameEditResultModel;
import ru.hits.just_4sport.model.api.schedule.ScheduleEditModel;
import ru.hits.just_4sport.service.EventAuthorService;

import java.util.List;
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

    @PutMapping("/{id}/schedule")
    public ResponseEntity<Void> editEventSchedule(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id,
            @RequestBody ScheduleEditModel scheduleData
    ) {
        eventAuthorService.editEventSchedule(user.getUsername(), id, scheduleData);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/table")
    public ResponseEntity<Void> editTournamentTable(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id,
            @RequestBody List<GameEditResultModel> results
    ) {
        eventAuthorService.editTournamentTable(user.getUsername(), id, results);

        return ResponseEntity.ok().build();
    }

    @PostMapping(
            value = "/{id}/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> setEventPhoto(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id,
            @RequestPart("file") MultipartFile photo
    ) {
        eventAuthorService.setEventPhoto(user.getUsername(), id, photo);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/photo")
    public ResponseEntity<Void> deleteEventPhoto(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id
    ) {
        eventAuthorService.deleteEventPhoto(user.getUsername(), id);

        return ResponseEntity.ok().build();
    }
}
