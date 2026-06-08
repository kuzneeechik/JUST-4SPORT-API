package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.IdModel;
import ru.hits.just_4sport.model.api.event.EventCreateModel;
import ru.hits.just_4sport.model.api.event.EventFilterModel;
import ru.hits.just_4sport.model.api.event.EventModel;
import ru.hits.just_4sport.model.api.event.EventShortModel;
import ru.hits.just_4sport.model.api.team.TeamModel;
import ru.hits.just_4sport.model.domain.EventEntity;
import ru.hits.just_4sport.model.domain.PhotoEntity;
import ru.hits.just_4sport.model.enums.SortDirection;
import ru.hits.just_4sport.model.enums.SortField;
import ru.hits.just_4sport.model.mapper.*;
import ru.hits.just_4sport.repository.EventRepository;
import ru.hits.just_4sport.repository.UserRepository;
import ru.hits.just_4sport.utils.EventSpecificationUtility;

import java.time.LocalDateTime;
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
    private final PhotoService photoService;

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

    public IdModel createEvent(
            String email,
            EventCreateModel event,
            MultipartFile photo
    ) {
        var author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Автор не найден среди пользователей"));

        if (event.getDateStart().isBefore(LocalDateTime.now()) ||
                event.getDateEnd().isBefore(LocalDateTime.now()) ||
                event.getDateEnd().isBefore(event.getDateStart())) {
            throw new BadRequestException("Некорректная дата начала или окончания мероприятия");
        }

        if (event.getDeadline().isBefore(LocalDateTime.now()) ||
                event.getDeadline().isAfter(event.getDateStart())) {
            throw new BadRequestException("Некорректная дата дедлайна");
        }

        var eventEntity = new EventEntity()
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
                .setTeamsNumber(event.getTeamsNumber())
                .setAuthor(author);

        if (photo != null) {
            var photoEntity = setPhoto(photo);

            eventEntity.setPhoto(photoEntity);
        }

        eventRepository.save(eventEntity);

        return new IdModel().setId(eventEntity.getId());
    }

    private Sort getSort(SortField sortField, SortDirection sortDirection) {
        var direction = SortDirection.DESC.equals(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;

        return switch (sortField) {
            case DATE -> Sort.by(direction, "dateStart");
            case SortField.COST -> Sort.by(direction, "cost");
        };
    }

    private PhotoEntity setPhoto(MultipartFile photo) {
        photoService.validateImage(photo);

        String photoName = photoService.saveImage(photo);

        return new PhotoEntity()
                .setTitle(photo.getOriginalFilename())
                .setPath(photoName);
    }
}
