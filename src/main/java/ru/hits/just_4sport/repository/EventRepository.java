package ru.hits.just_4sport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hits.just_4sport.model.domain.EventEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventEntity, UUID>,
        JpaSpecificationExecutor<EventEntity> {
    Optional<EventEntity> findEventEntitiesById(UUID id);

    @Query("""
        select distinct e
        from EventEntity e
            join e.teams t
            left join t.teamMembers m
        where t.captain.id = :userId
            or m.id = :userId
    """)
    List<EventEntity> findEventEntitiesByAnyTeamIn(@Param("userId") UUID userId);

    List<EventEntity> findAllByDateStartBetween(LocalDateTime dateStartAfter, LocalDateTime dateStartBefore);
}
