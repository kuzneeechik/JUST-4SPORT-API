package ru.hits.just_4sport.model.api.team;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.hits.just_4sport.model.api.user.UserModel;

import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class TeamAuthorModel {

    private UUID id;

    private String name;

    private UserModel captain;

    private List<UserModel> teamMembers;

    private String contactInformation;
}
