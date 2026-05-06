package ru.hits.just_4sport.model.api.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TokenModel {

    private String accessToken;
    private String refreshToken;
}
