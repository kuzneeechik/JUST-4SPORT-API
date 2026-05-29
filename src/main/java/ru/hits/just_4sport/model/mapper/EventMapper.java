package ru.hits.just_4sport.model.mapper;

import org.mapstruct.Mapper;
import ru.hits.just_4sport.model.api.event.EventShortModel;
import ru.hits.just_4sport.model.domain.EventEntity;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventShortModel toShortModel(EventEntity event);
}
