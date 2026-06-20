package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.team.TeamApplicationModel;
import ru.hits.just_4sport.model.domain.TeamEntity;
import ru.hits.just_4sport.model.domain.UserEntity;
import ru.hits.just_4sport.model.enums.EventStatus;
import ru.hits.just_4sport.repository.EventRepository;
import ru.hits.just_4sport.repository.TeamRepository;
import ru.hits.just_4sport.repository.UserRepository;
import ru.hits.just_4sport.service.notification.event.NewApplicationEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void sendApplication(UUID id, TeamApplicationModel teamApplication) {
        var event = eventRepository.findEventEntitiesById(id)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        if (event.getTeamsNumber() == event.getTeams().size() ||
                event.getEventStatus() == EventStatus.CANCELLED ||
                event.getEventStatus() != EventStatus.WILL_BE ||
                event.getDeadline().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Набор команд на это мероприятие уже завершён");
        }

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

        publisher.publishEvent(new NewApplicationEvent(team.getId(), event.getId()));
    }

    public void cancelApplication(UUID id, String userEmail) {
        var event = eventRepository.findEventEntitiesById(id)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (event.getDeadline().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("После дедлайна нельзя отменить регистрацию на мероприятие");
        }

        var team = event.getTeams().stream()
                .filter(currentTeam -> currentTeam.getCaptain().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Заявка команды на мероприятие не найдена"));

        event.getTeams().remove(team);

        team.getTeamMembers().clear();
        teamRepository.save(team);
        teamRepository.delete(team);
    }
}
