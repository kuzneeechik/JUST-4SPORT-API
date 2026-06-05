package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.event.EventFilterModel;
import ru.hits.just_4sport.model.api.event.EventModel;
import ru.hits.just_4sport.model.api.event.EventShortModel;
import ru.hits.just_4sport.model.api.team.TeamApplicationModel;
import ru.hits.just_4sport.model.api.team.TeamModel;
import ru.hits.just_4sport.model.domain.EventEntity;
import ru.hits.just_4sport.model.domain.TeamEntity;
import ru.hits.just_4sport.model.domain.UserEntity;
import ru.hits.just_4sport.model.enums.SortDirection;
import ru.hits.just_4sport.model.enums.SortField;
import ru.hits.just_4sport.model.mapper.*;
import ru.hits.just_4sport.repository.EventRepository;
import ru.hits.just_4sport.repository.TeamRepository;
import ru.hits.just_4sport.repository.UserRepository;
import ru.hits.just_4sport.utils.EventSpecificationUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventUserService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CommentMapper commentMapper;
    private final ScheduleMapper scheduleMapper;
    private final PhotoMapper photoMapper;
    private final TeamMapper teamMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

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

        return eventRepository.findAll(filtering, pageable)
                .map(eventMapper::toShortModel);
    }

    public EventModel getEventById(UUID id) {
        var event = eventRepository.findEventEntitiesById(id)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        return eventFactory(event);
    }

    public List<TeamModel> getParticipants(UUID id) {
        var event = eventRepository.findEventEntitiesById(id)
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

    public void sendApplication(UUID id, TeamApplicationModel teamApplication) {
        var event = eventRepository.findEventEntitiesById(id)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        var captain = userRepository.findByNickname(teamApplication.getCaptainNickname())
                .orElseThrow(() -> new NotFoundException("Капитан команды не был найден"));

        var members = new ArrayList<UserEntity>();

        for (var memberNickname : teamApplication.getMembersNicknames()) {
            var currentMember = userRepository.findByNickname(memberNickname)
                    .orElseThrow(() ->
                            new NotFoundException("Член команды " + memberNickname + " не был найден"));

            members.add(currentMember);
        }

        var memberIds = members.stream()
                .map(UserEntity::getId)
                .toList();

        if (teamRepository.existsTeamWithUsersInEvent(id, memberIds)) {
            throw new BadRequestException("Один из членов команды уже зарегистрирован" +
                    " на мероприятие в составе другой команды");
        }

        var team = new TeamEntity()
                .setEvent(event)
                .setName(teamApplication.getName())
                .setCaptain(captain)
                .setTeamMembers(members)
                .setContactInformation(teamApplication.getContactInformation());

        teamRepository.save(team);

        event.getTeams().add(team);

        eventRepository.save(event);
    }

    public void cancelApplication(UUID id, String userEmail) {
        var event = eventRepository.findEventEntitiesById(id)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        event.getTeams().removeIf(team -> team.getCaptain().equals(user));

        eventRepository.save(event);
    }

    private Sort getSort(SortField sortField, SortDirection sortDirection) {
        var direction = SortDirection.DESC.equals(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;

        return switch (sortField) {
            case DATE -> Sort.by(direction, "dateStart");
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
