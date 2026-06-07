package ru.hits.just_4sport.model.api.event;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.hits.just_4sport.model.enums.EventType;
import ru.hits.just_4sport.model.enums.SkillLevel;
import ru.hits.just_4sport.model.enums.Sport;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EventCreateModel {

    @NotBlank(message = "Название мероприятия обязательно")
    @Size(min = 2, message = "Слишком короткое название")
    @Size(max = 255, message = "Слишком длинное название")
    private String name;

    @Size(max = 2000, message = "Слишком длинное описание")
    private String description;

    @NotNull(message = "Указать дату начала мероприятия обязательно")
    private LocalDateTime dateStart;

    @NotNull(message = "Указать дату окончания мероприятия обязательно")
    private LocalDateTime dateEnd;

    @NotBlank(message = "Необходимо указать место проведения мероприятия")
    @Size(min = 2, message = "Слишком короткое место")
    @Size(max = 255, message = "Слишком длинное место")
    private String place;

    @NotNull(message = "Необходимо указать стоимость участия в мероприятии")
    @DecimalMin(value = "0.0", message = "Стоимость не может быть меньше нуля")
    private BigDecimal cost;

    @NotNull(message = "Необходимо указать вид спорта")
    private Sport sport;

    @NotNull(message = "Необходимо указать вид активности")
    private EventType eventType;

    @NotNull(message = "Необходимо указать уровень подготовки")
    private SkillLevel skillLevel;

    @NotNull(message = "Необходимо указать дедлайн для подачи заявки на участие")
    private LocalDateTime deadline;

    @NotNull(message = "Необходимо указать количество команд, которые будут участвовать в мероприятии")
    @Min(value = 1, message = "К участию должна приглашаться хотя бы одна команда")
    private Integer teamsNumber;
}
