package ru.hits.just_4sport.model.api.event;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.hits.just_4sport.model.api.*;
import ru.hits.just_4sport.model.api.comment.CommentModel;
import ru.hits.just_4sport.model.api.schedule.ScheduleModel;
import ru.hits.just_4sport.model.api.team.TeamGameModel;
import ru.hits.just_4sport.model.api.user.UserModel;
import ru.hits.just_4sport.model.enums.EventStatus;
import ru.hits.just_4sport.model.enums.EventType;
import ru.hits.just_4sport.model.enums.SkillLevel;
import ru.hits.just_4sport.model.enums.Sport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class EventModel {

    private UUID id;

    private EventStatus eventStatus;

    private String name;

    private String description;

    private LocalDateTime dateStart;

    private LocalDateTime dateEnd;

    private String place;

    private BigDecimal cost;

    private Sport sport;

    private EventType eventType;

    private SkillLevel skillLevel;

    private UserModel author;

    private PhotoModel photo;

    private ScheduleModel schedule;

    private List<TeamGameModel> teams;

    private LocalDateTime deadline;

    private Integer teamsNumber;

    private List<CommentModel> comments;
}
