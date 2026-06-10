package ru.hits.just_4sport.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "games")
@Accessors(chain = true)
public class GameEntity {

    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    @Column(nullable = false)
    private LocalDateTime date;

    private String result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_participant_id", nullable = false)
    private TeamEntity firstParticipant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_participant_id", nullable = false)
    private TeamEntity secondParticipant;
}
