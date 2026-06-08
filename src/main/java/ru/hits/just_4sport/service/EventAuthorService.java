package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.event.EventEditModel;
import ru.hits.just_4sport.model.api.team.TeamAuthorModel;
import ru.hits.just_4sport.model.mapper.TeamMapper;
import ru.hits.just_4sport.model.mapper.UserMapper;
import ru.hits.just_4sport.repository.EventRepository;
import ru.hits.just_4sport.repository.UserRepository;

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

    public List<TeamAuthorModel> getParticipants(String email, UUID id) {
        var event = eventRepository.findEventEntitiesById(id)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        var author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Автор не найден"));

        if (!author.getId().equals(event.getAuthor().getId())) {
            throw new BadRequestException("Пользователь не является автором мероприятия");
        }

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

    public void editEvent(String email, UUID id, EventEditModel eventData) {
        var event = eventRepository.findEventEntitiesById(id)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        var author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Автор не найден"));

        if (!author.getId().equals(event.getAuthor().getId())) {
            throw new BadRequestException("Пользователь не является автором мероприятия");
        }

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
}
