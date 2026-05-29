package ru.hits.just_4sport.model.api.event;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.hits.just_4sport.model.domain.PhotoEntity;
import ru.hits.just_4sport.model.enums.EventStatus;
import ru.hits.just_4sport.model.enums.EventType;
import ru.hits.just_4sport.model.enums.SkillLevel;
import ru.hits.just_4sport.model.enums.Sport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class EventShortModel {

    private UUID id;

    private EventStatus eventStatus;

    private String name;

    private LocalDateTime dateStart;

    private LocalDateTime dateEnd;

    private BigDecimal cost;

    private Sport sport;

    private EventType eventType;

    private SkillLevel skillLevel;

    private PhotoEntity photo;
}
