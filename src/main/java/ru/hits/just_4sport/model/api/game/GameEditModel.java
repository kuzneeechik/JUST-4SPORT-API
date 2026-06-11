package ru.hits.just_4sport.model.api.game;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class GameEditModel {

    private LocalDateTime date;

    private UUID firstParticipantId;

    private UUID secondParticipantId;
}
