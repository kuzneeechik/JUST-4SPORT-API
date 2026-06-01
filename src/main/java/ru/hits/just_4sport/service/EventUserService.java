package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.event.EventFilterModel;
import ru.hits.just_4sport.model.api.event.EventModel;
import ru.hits.just_4sport.model.api.event.EventShortModel;
import ru.hits.just_4sport.model.api.team.TeamModel;
import ru.hits.just_4sport.model.domain.EventEntity;
import ru.hits.just_4sport.model.enums.SortDirection;
import ru.hits.just_4sport.model.enums.SortField;
import ru.hits.just_4sport.model.mapper.*;
import ru.hits.just_4sport.repository.EventsRepository;
import ru.hits.just_4sport.utils.EventSpecificationUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventUserService {

    private final EventsRepository eventsRepository;
    private final EventMapper eventMapper;
    private final CommentMapper commentMapper;
    private final ScheduleMapper scheduleMapper;
    private final PhotoMapper photoMapper;
    private final TeamMapper teamMapper;
    private final UserMapper userMapper;

    public Page<EventShortModel> getFilteredEvents(
            EventFilterModel filter,
            SortField sortField,
            SortDirection sortDirection,
            int page,
            int size
    ) {
        var filtering = EventSpecificationUtility.getFilteredEvents(filter);

        var pageable = PageRequest.of(
                page,
                size,
                getSort(sortField, sortDirection));

        return eventsRepository.findAll(filtering, pageable)
                .map(eventMapper::toShortModel);
    }

    public EventModel getEventById(UUID id) {
        var event = eventsRepository.findEventEntitiesById(id)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        return eventFactory(event);
    }

    public List<TeamModel> getParticipants(UUID id) {
        var event = eventsRepository.findEventEntitiesById(id)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        var teams = new ArrayList<TeamModel>();

        for (var team : event.getTeams()) {
            var members = team.getTeamMembers().stream()
                    .map(userMapper::toModel)
                    .toList();

            var captain = userMapper.toModel(team.getCaptain());

            teams.add(teamMapper.toModel(team, captain, members));
        }

        return teams;
    }

    private Sort getSort(SortField sortField, SortDirection sortDirection) {
        var direction = SortDirection.DESC.equals(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;

        return switch (sortField) {
            case DATE -> Sort.by(direction, "startDate");
            case SortField.COST -> Sort.by(direction, "cost");
        };
    }

    private EventModel eventFactory(EventEntity event) {
        var photo = photoMapper.toModel(event.getPhoto());

        var comments = event.getComments().stream()
                .map(commentMapper::toModel)
                .toList();

        var schedule = scheduleMapper.toModel(event.getSchedule());

        var teams =  event.getTeams().stream()
                .map(teamMapper::toGameModel)
                .toList();

        var author = userMapper.toModel(event.getAuthor());

        return eventMapper.toModel(
                event,
                author,
                photo,
                comments,
                schedule,
                teams);
    }
}
