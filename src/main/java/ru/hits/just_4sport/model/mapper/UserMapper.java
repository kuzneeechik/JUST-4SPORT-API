package ru.hits.just_4sport.model.mapper;

import org.mapstruct.Mapper;
import ru.hits.just_4sport.model.api.user.UserModel;
import ru.hits.just_4sport.model.domain.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserModel toModel(UserEntity user);
}
