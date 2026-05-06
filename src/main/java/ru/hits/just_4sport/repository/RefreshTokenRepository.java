package ru.hits.just_4sport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hits.just_4sport.model.domain.RefreshTokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);
}
