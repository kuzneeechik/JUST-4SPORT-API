package ru.hits.just_4sport.model.api.auth;

import lombok.Data;
import ru.hits.just_4sport.model.enums.Sport;

import java.util.List;

@Data
public class RegistrationModel {

    private String name;

    private String nickname;

    private String email;

    private String password;

    private List<Sport> favoriteSports;
}
