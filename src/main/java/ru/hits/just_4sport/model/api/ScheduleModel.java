package ru.hits.just_4sport.model.api;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ScheduleModel {

    private UUID id;

    private List<GameModel> games;
}
