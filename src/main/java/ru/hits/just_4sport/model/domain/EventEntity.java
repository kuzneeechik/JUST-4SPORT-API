package ru.hits.just_4sport.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.hits.just_4sport.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "events")
@Accessors(chain = true)
public class EventEntity {

    @Id
    private UUID id = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus eventStatus = EventStatus.WILL_BE;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "date_start", nullable = false)
    private LocalDateTime dateStart;

    @Column(name = "date_end", nullable = false)
    private LocalDateTime dateEnd;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sport sport;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillLevel skillLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private PhotoEntity photo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<TeamEntity> teams = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    private Integer teamsNumber;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<CommentEntity> comments = new ArrayList<>();
}
