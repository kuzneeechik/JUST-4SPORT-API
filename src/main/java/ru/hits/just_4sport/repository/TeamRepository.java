package ru.hits.just_4sport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hits.just_4sport.model.domain.TeamEntity;

import java.util.Collection;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<TeamEntity, UUID> {
    @Query("""
        select count(t) > 0
        from TeamEntity t
        left join t.teamMembers member
        where t.event.id = :eventId
          and (
              t.captain.id in :userIds
              or member.id in :userIds
          )
        """)
    boolean existsTeamWithUsersInEvent(
            @Param("eventId") UUID eventId,
            @Param("userIds") Collection<UUID> userIds
    );
}