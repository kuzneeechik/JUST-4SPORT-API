package ru.hits.just_4sport.model.api.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    private String description;

    @NotBlank(message = "Указать дату начала мероприятия обязательно")
    private LocalDateTime dateStart;

    @NotBlank(message = "Указать дату окончания мероприятия обязательно")
    private LocalDateTime dateEnd;

    @NotBlank(message = "Необходимо указать место проведения мероприятия")
    @Size(min = 2, message = "Слишком короткое место")
    @Size(max = 255, message = "Слишком длинное место")
    private String place;

    @NotBlank(message = "Необходимо указать стоимость участия в мероприятии")
    @Min(value = 0, message = "Стоимость не может быть меньше нуля")
    private BigDecimal cost;

    @NotBlank(message = "Необходимо указать вид спорта")
    private Sport sport;

    @NotBlank(message = "Необходимо указать вид активности")
    private EventType eventType;

    @NotBlank(message = "Необходимо указать уровень подготовки")
    private SkillLevel skillLevel;

    @NotBlank(message = "Необходимо указать дедлайн для подачи заявки на участие")
    private LocalDateTime deadline;

    @NotBlank(message = "Необходимо указать количество команд, которые будут участвовать в мероприятии")
    @Min(value = 1, message = "К участию должна приглашаться хотя бы одна команда")
    private Integer teamsNumber;
}
