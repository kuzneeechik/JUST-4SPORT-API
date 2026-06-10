package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.infrastructure.exception.ForbiddenAccessException;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.event.EventEditModel;
import ru.hits.just_4sport.model.api.team.TeamAuthorModel;
import ru.hits.just_4sport.model.domain.EventEntity;
import ru.hits.just_4sport.model.enums.EventStatus;
import ru.hits.just_4sport.model.enums.EventType;
import ru.hits.just_4sport.model.mapper.TeamMapper;
import ru.hits.just_4sport.model.mapper.UserMapper;
import ru.hits.just_4sport.repository.EventRepository;
import ru.hits.just_4sport.repository.TeamRepository;
import ru.hits.just_4sport.repository.UserRepository;
import ru.hits.just_4sport.service.auth.ScheduleService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventAuthorService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TeamMapper teamMapper;
    private final TeamRepository teamRepository;
    private final ScheduleService scheduleService;

    public void editEvent(String email, UUID id, EventEditModel eventData) {
        var event = findEventAndCheckAuthor(email, id);

        event.setName(eventData.getName())
                .setDescription(eventData.getDescription())
                .setDateStart(eventData.getDateStart())
                .setDateEnd(eventData.getDateEnd())
                .setPlace(eventData.getPlace())
                .setCost(eventData.getCost())
                .setDeadline(eventData.getDeadline())
                .setTeamsNumber(eventData.getTeamsNumber());

        eventRepository.save(event);
    }

    public void deleteEvent(String email, UUID id) {
        var event = findEventAndCheckAuthor(email, id);

        eventRepository.delete(event);
    }

    public void cancelEvent(String email, UUID id) {
        var event = findEventAndCheckAuthor(email, id);

        event.setEventStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
    }

    public void finishEvent(String email, UUID id) {
        var event = findEventAndCheckAuthor(email, id);

        if (event.getEventStatus() != EventStatus.UNDERWAY ||
            event.getDateEnd().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Нельзя отметить мероприятие завершённым, если оно не было проведено");
        }

        event.setEventStatus(EventStatus.FINISHED);
        eventRepository.save(event);
    }

    public List<TeamAuthorModel> getParticipants(String email, UUID id) {
        var event = findEventAndCheckAuthor(email, id);

        var participants = new ArrayList<TeamAuthorModel>();

        for (var team : event.getTeams()) {
            var captain = userMapper.toModel(team.getCaptain());

            var teamMembers = team.getTeamMembers().stream()
                    .map(userMapper::toModel)
                    .toList();

            participants.add(teamMapper.toAuthorModel(team, captain, teamMembers));
        }

        return participants;
    }

    public void deleteParticipant(String email, UUID eventId, UUID teamId) {
        var event = findEventAndCheckAuthor(email, eventId);

        var team = teamRepository.findById(teamId)
                        .orElseThrow(() -> new NotFoundException("Команда не найдена"));

        if (!team.getEvent().getId().equals(event.getId())) {
            throw new ForbiddenAccessException("Невозможно удалить команду, не относящуюся к этому мероприятию");
        }

        event.getTeams().removeIf(currentTeam -> currentTeam.getId().equals(team.getId()));

        team.getTeamMembers().clear();
        teamRepository.save(team);

        teamRepository.delete(team);
    }

    public void closeRecruitment(String email, UUID id) {
        var event = findEventAndCheckAuthor(email, id);

        if (event.getEventStatus() != EventStatus.WILL_BE) {
            throw new BadRequestException("Набор участников уже закрыт");
        }

        event.setEventStatus(EventStatus.UNDERWAY);

        if (event.getEventType() == EventType.TOURNAMENT) {
            event.setSchedule(scheduleService.generateSchedule(event));
        }

        eventRepository.save(event);
    }

    private EventEntity findEventAndCheckAuthor(String email, UUID id) {
        var event = eventRepository.findEventEntitiesById(id)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        var author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Автор не найден"));

        if (!author.getId().equals(event.getAuthor().getId())) {
            throw new ForbiddenAccessException("Пользователь не является автором мероприятия");
        }

        return event;
    }
}
