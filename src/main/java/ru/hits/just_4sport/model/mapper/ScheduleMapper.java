package ru.hits.just_4sport.model.mapper;

import org.mapstruct.Mapper;
import ru.hits.just_4sport.model.api.ScheduleModel;
import ru.hits.just_4sport.model.domain.ScheduleEntity;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    ScheduleModel toModel(ScheduleEntity schedule);
}
