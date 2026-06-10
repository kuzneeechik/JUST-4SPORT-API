package ru.hits.just_4sport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hits.just_4sport.model.domain.ScheduleEntity;

import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, UUID> {
}
