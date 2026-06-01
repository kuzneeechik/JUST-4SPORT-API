package ru.hits.just_4sport.model.mapper;

import org.mapstruct.Mapper;
import ru.hits.just_4sport.model.api.team.TeamGameModel;
import ru.hits.just_4sport.model.api.team.TeamModel;
import ru.hits.just_4sport.model.api.user.UserModel;
import ru.hits.just_4sport.model.domain.TeamEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamGameModel toGameModel(TeamEntity team);

    default TeamModel toModel(
            TeamEntity team,
            UserModel captain,
            List<UserModel> members
    ) {
        return new TeamModel()
                .setId(team.getId())
                .setName(team.getName())
                .setCaptain(captain)
                .setTeamMembers(members);
    }
}
