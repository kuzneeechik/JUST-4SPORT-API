package ru.hits.just_4sport.model.api.auth;

import lombok.Data;

@Data
public class LoginModel {

    private String email;

    private String password;
}
