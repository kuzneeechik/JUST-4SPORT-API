package ru.hits.just_4sport.model.api.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class GameEditResultModel {

    @NotNull(message = "Необходимо указать id игры")
    private UUID id;

    @NotBlank(message = "Необходимо указать результат игры")
    @Pattern(
            regexp = "^\\d+:\\d+$",
            message = "Результат игры описывается числами через двоеточие"
    )
    private String result;
}
