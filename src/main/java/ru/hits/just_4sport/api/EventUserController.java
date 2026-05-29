package ru.hits.just_4sport.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.just_4sport.model.api.event.EventFilterModel;
import ru.hits.just_4sport.model.api.event.EventShortModel;
import ru.hits.just_4sport.model.enums.*;
import ru.hits.just_4sport.service.EventUserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}
