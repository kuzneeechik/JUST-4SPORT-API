package ru.hits.just_4sport.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.uploads")
public record UploadProperties(String profilePhotosDir) {}