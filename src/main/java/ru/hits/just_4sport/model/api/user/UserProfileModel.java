package ru.hits.just_4sport.model.api.user;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.hits.just_4sport.model.api.PhotoModel;
import ru.hits.just_4sport.model.domain.EventEntity;
import ru.hits.just_4sport.model.enums.Sport;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserProfileModel {

    private String name;

    private String nickname;

    private String email;

    private PhotoModel photo;

    private List<Sport> favoriteSports;

    private List<EventEntity> authorEvents;

    private List<EventEntity> participantEvents;
}
