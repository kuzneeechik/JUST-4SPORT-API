package ru.hits.just_4sport.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties (
    String secret,
    int accessTokenExpirationMinutes,
    int refreshTokenExpirationDays
) {
}
