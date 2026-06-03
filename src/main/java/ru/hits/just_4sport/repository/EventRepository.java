package ru.hits.just_4sport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hits.just_4sport.model.domain.EventEntity;
import ru.hits.just_4sport.model.domain.TeamEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventEntity, UUID>,
        JpaSpecificationExecutor<EventEntity> {
    Optional<EventEntity> findEventEntitiesById(UUID id);

    @Query("""
    select e
    from EventEntity e
        join e.teams t
    where t in :teams
    """)
    List<EventEntity> findEventEntitiesByAnyTeamIn(@Param("teams") List<TeamEntity> teams);
}
