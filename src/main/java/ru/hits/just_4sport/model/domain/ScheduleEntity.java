package ru.hits.just_4sport.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "schedules")
public class ScheduleEntity {

    @Id
    private UUID id = UUID.randomUUID();

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<GameEntity> games = new ArrayList<>();
}
