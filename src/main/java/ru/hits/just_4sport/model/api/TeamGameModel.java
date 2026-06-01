package ru.hits.just_4sport.model.api;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class TeamGameModel {

    private UUID id;

    private String name;
}
