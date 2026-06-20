package ru.hits.just_4sport.service.notification.event;

import java.util.UUID;

public record NewApplicationEvent(UUID teamId, UUID eventId) {}
