package ru.hits.just_4sport.model.api.schedule;

import lombok.Data;
import ru.hits.just_4sport.model.api.game.GameEditModel;

import java.util.List;

@Data
public class ScheduleEditModel {

    List<GameEditModel> games;
}
