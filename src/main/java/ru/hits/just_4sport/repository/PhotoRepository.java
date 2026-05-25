package ru.hits.just_4sport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hits.just_4sport.model.domain.PhotoEntity;

import java.util.UUID;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {
}
