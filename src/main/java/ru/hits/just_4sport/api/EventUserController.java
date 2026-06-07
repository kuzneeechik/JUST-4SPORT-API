package ru.hits.just_4sport.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hits.just_4sport.model.api.IdModel;
import ru.hits.just_4sport.model.api.event.EventCreateModel;
import ru.hits.just_4sport.model.api.event.EventFilterModel;
import ru.hits.just_4sport.model.api.event.EventModel;
import ru.hits.just_4sport.model.api.event.EventShortModel;
import ru.hits.just_4sport.model.api.team.TeamApplicationModel;
import ru.hits.just_4sport.model.api.team.TeamModel;
import ru.hits.just_4sport.model.enums.*;
import ru.hits.just_4sport.service.EventUserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/events")
@RequiredArgsConstructor
public class EventUserController {

    private final EventUserService eventUserService;

    @GetMapping
    public ResponseEntity<Page<EventShortModel>> getEvents(
            @RequestParam(required = false) EventStatus status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDateTime dateStart,
            @RequestParam(required = false) LocalDateTime dateEnd,
            @RequestParam(required = false) BigDecimal costStart,
            @RequestParam(required = false) BigDecimal costEnd,
            @RequestParam(required = false) Sport sport,
            @RequestParam(required = false) EventType eventType,
            @RequestParam(required = false) SkillLevel skillLevel,
            @RequestParam(defaultValue = "DATE") SortField sortField,
            @RequestParam(defaultValue = "ASC") SortDirection sortDirection,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        var filter = new EventFilterModel()
                .setEventStatus(status)
                .setName(name)
                .setDateStart(dateStart)
                .setDateEnd(dateEnd)
                .setCostStart(costStart)
                .setCostEnd(costEnd)
                .setSport(sport)
                .setEventType(eventType)
                .setSkillLevel(skillLevel);

        return ResponseEntity.ok(eventUserService.getFilteredEvents(
                filter,
                sortField,
                sortDirection,
                page,
                size
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventModel> getEventById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventUserService.getEventById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<IdModel> createEvent(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestPart("event") EventCreateModel event,
            @RequestPart(value = "file", required = false) MultipartFile photo
    ) {
        return ResponseEntity.ok(eventUserService.createEvent(user.getUsername(), event, photo));
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<TeamModel>> getParticipants(@PathVariable UUID id) {
        return ResponseEntity.ok(eventUserService.getParticipants(id));
    }

    @PostMapping("/{id}/application")
    public ResponseEntity<Void> sendApplication(
            @PathVariable UUID id,
            @RequestBody TeamApplicationModel teamApplication
    ) {
        eventUserService.sendApplication(id, teamApplication);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/application")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails user
    ) {
        eventUserService.cancelApplication(id, user.getUsername());

        return ResponseEntity.ok().build();
    }
}
