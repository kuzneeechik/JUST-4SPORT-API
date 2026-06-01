package ru.hits.just_4sport.model.api.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class UserModel {

    private UUID id;

    private String name;

    private String nickname;
}
