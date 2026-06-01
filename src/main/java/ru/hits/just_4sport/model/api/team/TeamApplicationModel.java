package ru.hits.just_4sport.model.api.team;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class TeamApplicationModel {

    @NotBlank(message = "Название команды обязательно")
    private String name;

    @NotBlank(message = "Необходимо указать капитана команды")
    private String captainNickname;

    @NotBlank(message = "Необходимо указать членов команды")
    private List<String> membersNicknames;

    @NotBlank(message = "Необходимо указать контактную информацию")
    private String contactInformation;
}
