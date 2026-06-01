package ru.hits.just_4sport.model.api;

import lombok.Data;
import lombok.experimental.Accessors;

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
