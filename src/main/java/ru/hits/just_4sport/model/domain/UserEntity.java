package ru.hits.just_4sport.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.hits.just_4sport.model.enums.Sport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
@Accessors(chain = true)
public class UserEntity {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false,  unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private PhotoEntity photo;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "user_favorite_sports",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "sport", nullable = false)
    private List<Sport> favoriteSports = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<EventEntity> authorEvents = new ArrayList<>();

    @ManyToMany(mappedBy = "teams", fetch = FetchType.LAZY)
    private List<TeamEntity> teams = new ArrayList<>();
}
