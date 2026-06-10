package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.infrastructure.exception.ForbiddenAccessException;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.team.TeamAuthorModel;
import ru.hits.just_4sport.model.api.team.TeamModel;
import ru.hits.just_4sport.model.domain.EventEntity;
import ru.hits.just_4sport.model.enums.EventStatus;
import ru.hits.just_4sport.model.enums.EventType;
import ru.hits.just_4sport.model.mapper.TeamMapper;
import ru.hits.just_4sport.model.mapper.UserMapper;
import ru.hits.just_4sport.repository.EventRepository;
import ru.hits.just_4sport.repository.TeamRepository;
import ru.hits.just_4sport.repository.UserRepository;
import ru.hits.just_4sport.service.auth.ScheduleService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventParticipantsService {

    private final EventRepository eventRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ScheduleService scheduleService;
    private final UserMapper userMapper;
    private final TeamMapper teamMapper;

    public List<TeamModel> getParticipantsForUser(UUID id) {
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

    public List<TeamAuthorModel> getParticipantsForAuthor(String email, UUID id) {
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
