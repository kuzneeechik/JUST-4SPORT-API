package ru.hits.just_4sport.service.notification.event;

import java.util.UUID;

public record NewCommentEvent(UUID commentId, UUID eventId) {}
