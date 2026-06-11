package ru.hits.just_4sport.model.api.game;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.hits.just_4sport.model.api.team.TeamGameModel;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class GameModel {

    private UUID id;

    private LocalDateTime date;

    private String result;

    private TeamGameModel firstParticipant;

    private TeamGameModel secondParticipant;
}
