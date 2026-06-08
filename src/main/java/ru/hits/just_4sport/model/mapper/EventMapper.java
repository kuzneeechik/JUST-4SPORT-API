package ru.hits.just_4sport.model.mapper;

import org.mapstruct.Mapper;
import ru.hits.just_4sport.model.api.comment.CommentModel;
import ru.hits.just_4sport.model.api.PhotoModel;
import ru.hits.just_4sport.model.api.ScheduleModel;
import ru.hits.just_4sport.model.api.event.EventCreateModel;
import ru.hits.just_4sport.model.api.event.EventEditModel;
import ru.hits.just_4sport.model.api.team.TeamGameModel;
import ru.hits.just_4sport.model.api.event.EventModel;
import ru.hits.just_4sport.model.api.event.EventShortModel;
import ru.hits.just_4sport.model.api.user.UserModel;
import ru.hits.just_4sport.model.domain.EventEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventShortModel toShortModel(EventEntity event);

    default EventModel toModel(
            EventEntity event,
            UserModel author,
            PhotoModel photo,
            List<CommentModel> comments,
            ScheduleModel schedule,
            List<TeamGameModel> teams
    ) {
        if (event == null) {
            return null;
        }

        return new EventModel()
                .setId(event.getId())
                .setEventStatus(event.getEventStatus())
                .setName(event.getName())
                .setDescription(event.getDescription())
                .setDateStart(event.getDateStart())
                .setDateEnd(event.getDateEnd())
                .setPlace(event.getPlace())
                .setCost(event.getCost())
                .setSport(event.getSport())
                .setEventType(event.getEventType())
                .setSkillLevel(event.getSkillLevel())
                .setAuthor(author)
                .setPhoto(photo)
                .setSchedule(schedule)
                .setTeams(teams)
                .setDeadline(event.getDeadline())
                .setTeamsNumber(event.getTeamsNumber())
                .setComments(comments);
    }

    default EventEntity toEntity(EventCreateModel event) {
        if (event == null) {
            return null;
        }

        return new EventEntity()
                .setName(event.getName())
                .setDescription(event.getDescription())
                .setDateStart(event.getDateStart())
                .setDateEnd(event.getDateEnd())
                .setPlace(event.getPlace())
                .setCost(event.getCost())
                .setSport(event.getSport())
                .setEventType(event.getEventType())
                .setSkillLevel(event.getSkillLevel())
                .setDeadline(event.getDeadline())
                .setTeamsNumber(event.getTeamsNumber());
    }

    default EventEntity toEntity(EventEditModel event) {
        if (event == null) {
            return null;
        }

        return new EventEntity()
                .setName(event.getName())
                .setDescription(event.getDescription())
                .setDateStart(event.getDateStart())
                .setDateEnd(event.getDateEnd())
                .setPlace(event.getPlace())
                .setCost(event.getCost())
                .setDeadline(event.getDeadline())
                .setTeamsNumber(event.getTeamsNumber());
    }
}
