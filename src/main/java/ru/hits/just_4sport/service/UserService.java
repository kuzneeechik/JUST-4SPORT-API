package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.infrastructure.exception.BadRequest;
import ru.hits.just_4sport.model.api.TokenModel;
import ru.hits.just_4sport.model.api.user.UserRegistrationModel;
import ru.hits.just_4sport.model.domain.UserEntity;
import ru.hits.just_4sport.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public TokenModel registration(UserRegistrationModel user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BadRequest("Аккаунт с таким email уже существует");
        }

        var newUser = new UserEntity()
                .setName(user.getName())
                .setNickname(user.getNickname())
                .setEmail(user.getEmail())
                .setPasswordHash(passwordEncoder.encode(user.getPassword()))
                .setFavoriteSports(user.getFavoriteSports());

        userRepository.save(newUser);

        var accessToken = jwtService.generateToken(newUser);
        var refreshToken = refreshTokenService.createToken(newUser);

        return new TokenModel()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }
}
