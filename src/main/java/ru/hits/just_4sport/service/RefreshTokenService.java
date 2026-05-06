package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.domain.RefreshTokenEntity;
import ru.hits.just_4sport.model.domain.UserEntity;
import ru.hits.just_4sport.properties.JwtProperties;
import ru.hits.just_4sport.repository.RefreshTokenRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom =  new SecureRandom();

    public String createToken(UserEntity user) {
        var rawToken = generateToken();

        var refreshToken = new RefreshTokenEntity()
                .setTokenHash(hashToken(rawToken))
                .setUser(user)
                .setExpiresAt(LocalDateTime.now().plusDays(
                        jwtProperties.refreshTokenExpirationDays()
                ))
                .setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    public void revokeToken(String token, String email) {
        var tokenHash = refreshTokenRepository.findByTokenHash(hashToken(token))
                .orElseThrow(() -> new NotFoundException("Токен не найден"));

        if (!tokenHash.getUser().getEmail().equals(email)) {
            throw new BadRequestException("Токен не принадлежит пользователю");
        }

        if (tokenHash.isRevoked()) {
            return;
        }

        tokenHash.setRevoked(true);
        refreshTokenRepository.save(tokenHash);
    }

    public UserEntity revokeTokenAndReturnUser(String token) {
        var tokenHash = refreshTokenRepository.findByTokenHash(hashToken(token))
                .orElseThrow(() -> new NotFoundException("Токен не найден"));

        if (tokenHash.isRevoked()) {
            throw new BadRequestException("Токен отозван");
        }

        if (tokenHash.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Токен истёк");
        }

        tokenHash.setRevoked(true);
        refreshTokenRepository.save(tokenHash);

        return tokenHash.getUser();
    }

    private String hashToken(String rawToken) {

        return DigestUtils.sha256Hex(rawToken);
    }

    private String generateToken() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}
