package ru.hits.just_4sport.model.mapper;

import org.mapstruct.Mapper;
import ru.hits.just_4sport.model.api.PhotoModel;
import ru.hits.just_4sport.model.domain.PhotoEntity;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    PhotoModel toModel(PhotoEntity photo);
}
