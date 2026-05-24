package ru.hits.just_4sport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hits.just_4sport.model.domain.EventEntity;
import ru.hits.just_4sport.model.domain.TeamEntity;

import java.util.List;
import java.util.UUID;

public interface EventsRepository extends JpaRepository<EventEntity, UUID> {
    List<EventEntity> findEventEntitiesByTeams(List<TeamEntity> teams);
}
