package ru.hits.just_4sport.model.mapper;

import org.mapstruct.Mapper;
import ru.hits.just_4sport.model.api.TeamGameModel;
import ru.hits.just_4sport.model.domain.TeamEntity;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamGameModel toGameModel(TeamEntity team);
}
