package ru.hits.just_4sport.model.api.user;

import lombok.Data;

@Data
public class UserLoginModel {

    private String email;

    private String password;
}
