package ru.hits.just_4sport.model.api.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.hits.just_4sport.model.enums.Sport;

import java.util.List;

@Data
public class UserUpdateProfileModel {

    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, message = "Слишком короткое имя")
    @Size(max = 255, message = "Слишком длинное имя")
    private String name;

    @NotBlank(message = "Никнейм обязателен")
    @Pattern(
            regexp = "^[a-zA-Z0-9_.-]+$",
            message = "Никнейм может содержать только латинские буквы, цифры и _, -, ."
    )
    private String nickname;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    private String email;

    private List<Sport> favoriteSports;
}
