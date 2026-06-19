package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.infrastructure.exception.ForbiddenAccessException;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.event.EventEditModel;
import ru.hits.just_4sport.model.api.game.GameEditResultModel;
import ru.hits.just_4sport.model.api.schedule.ScheduleEditModel;
import ru.hits.just_4sport.model.domain.EventEntity;
import ru.hits.just_4sport.model.domain.GameEntity;
import ru.hits.just_4sport.model.domain.ScheduleEntity;
import ru.hits.just_4sport.model.enums.EventStatus;
import ru.hits.just_4sport.model.enums.EventType;
import ru.hits.just_4sport.repository.EventRepository;
import ru.hits.just_4sport.repository.PhotoRepository;
import ru.hits.just_4sport.repository.TeamRepository;
import ru.hits.just_4sport.repository.UserRepository;
import ru.hits.just_4sport.service.notification.event.EventCancelledEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventAuthorService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PhotoService photoService;
    private final PhotoRepository photoRepository;
    private final ApplicationEventPublisher publisher;

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

    @Transactional
    public void cancelEvent(String email, UUID id) {
        var event = findEventAndCheckAuthor(email, id);

        event.setEventStatus(EventStatus.CANCELLED);
        eventRepository.save(event);

        publisher.publishEvent(new EventCancelledEvent(id));
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

    public void editEventSchedule(String email, UUID id, ScheduleEditModel scheduleData) {
        var event = findEventAndCheckAuthor(email, id);

        if (event.getEventType() != EventType.TOURNAMENT) {
            throw new BadRequestException("Редактировать расписание можно только у турнира");
        }

        var schedule = new ScheduleEntity();

        var games = buildListOfGames(scheduleData, event, schedule);

        schedule.getGames().addAll(games);

        event.setSchedule(schedule);
        eventRepository.save(event);
    }

    public void editTournamentTable(String email, UUID id, List<GameEditResultModel> results) {
        var event = findEventAndCheckAuthor(email, id);

        if (event.getEventType() != EventType.TOURNAMENT) {
            throw new BadRequestException("Редактировать турнирную таблицу можно только у турнира");
        }

        if (event.getSchedule() == null) {
            throw new BadRequestException("У турнира еще нет расписания");
        }

        if (results == null || results.isEmpty()) {
            throw new BadRequestException("Необходимо передать результаты игр");
        }

        var games = event.getSchedule().getGames();

        for (var result : results) {
            var game = games.stream()
                    .filter(g -> g.getId().equals(result.getId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Игра не найдена"));

            game.setResult(result.getResult());
        }

        eventRepository.save(event);
    }

    public void setEventPhoto(String email, UUID id, MultipartFile photo) {
        var event = findEventAndCheckAuthor(email, id);

        photoService.validateImage(photo);

        var oldPhoto = event.getPhoto();

        var photoEntity = photoService.buildPhotoEntity(photo, oldPhoto);

        event.setPhoto(photoEntity);
        eventRepository.save(event);
    }

    public void deleteEventPhoto(String email, UUID id) {
        var event = findEventAndCheckAuthor(email, id);

        var photo = event.getPhoto();

        if (photo ==  null) {
            return;
        }

        photoService.deleteImage(photo.getPath());

        photoRepository.delete(photo);

        event.setPhoto(null);
        eventRepository.save(event);
    }

    public EventEntity findEventAndCheckAuthor(String email, UUID id) {
        var event = eventRepository.findEventEntitiesById(id)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        var author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Автор не найден"));

        if (!author.getId().equals(event.getAuthor().getId())) {
            throw new ForbiddenAccessException("Пользователь не является автором мероприятия");
        }

        return event;
    }

    private ArrayList<GameEntity> buildListOfGames(
            ScheduleEditModel scheduleData,
            EventEntity event,
            ScheduleEntity schedule
    ) {
        var games = new ArrayList<GameEntity>();

        for (var game : scheduleData.getGames()) {
            var firstParticipant = teamRepository.findById(game.getFirstParticipantId())
                    .orElseThrow(() -> new NotFoundException("Команда не найдена"));

            var secondParticipant = teamRepository.findById(game.getSecondParticipantId())
                    .orElseThrow(() -> new NotFoundException("Команда не найдена"));

            if (!firstParticipant.getEvent().getId().equals(event.getId())
                    || !secondParticipant.getEvent().getId().equals(event.getId())) {
                throw new BadRequestException("Команды должны принадлежать этому мероприятию");
            }

            if (firstParticipant.getId().equals(secondParticipant.getId())) {
                throw new BadRequestException("Команда не может играть сама с собой");
            }

            if (game.getDate().isBefore(event.getDateStart()) || game.getDate().isAfter(event.getDateEnd())) {
                throw new BadRequestException("Игра должна проходить в рамках дат мероприятия");
            }

            var gameEntity = new GameEntity()
                    .setSchedule(schedule)
                    .setDate(game.getDate())
                    .setFirstParticipant(firstParticipant)
                    .setSecondParticipant(secondParticipant);

            games.add(gameEntity);
        }

        return games;
    }
}
