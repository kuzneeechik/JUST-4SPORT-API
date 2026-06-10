package ru.hits.just_4sport.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.model.domain.EventEntity;
import ru.hits.just_4sport.model.domain.GameEntity;
import ru.hits.just_4sport.model.domain.ScheduleEntity;
import ru.hits.just_4sport.model.domain.TeamEntity;
import ru.hits.just_4sport.repository.ScheduleRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleEntity generateSchedule(EventEntity event) {
        var schedule = new ScheduleEntity();

        var teams = event.getTeams();

        if (teams.size() < 2) {
            throw new BadRequestException("Для создания расписания нужно минимум две команды");
        }

        var games = generateGames(teams, event, schedule);

        Collections.shuffle(games);

        schedule.getGames().addAll(games);
        scheduleRepository.save(schedule);

        return schedule;
    }

    private ArrayList<GameEntity> generateGames(
            List<TeamEntity> teams,
            EventEntity event,
            ScheduleEntity schedule
    ) {
        var games = new ArrayList<GameEntity>();

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                var game = new GameEntity()
                        .setSchedule(schedule)
                        .setDate(event.getDateStart())
                        .setFirstParticipant(teams.get(i))
                        .setSecondParticipant(teams.get(j));

                games.add(game);
            }
        }

        return games;
    }
}
