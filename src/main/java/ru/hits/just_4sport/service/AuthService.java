package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.model.api.auth.*;
import ru.hits.just_4sport.model.domain.UserEntity;
import ru.hits.just_4sport.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public TokenModel registration(RegistrationModel user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BadRequestException("Аккаунт с таким email уже существует");
        }

        var newUser = new UserEntity()
                .setName(user.getName())
                .setNickname(user.getNickname())
                .setEmail(user.getEmail())
                .setPasswordHash(passwordEncoder.encode(user.getPassword()))
                .setFavoriteSports(user.getFavoriteSports());

        userRepository.save(newUser);

        return createTokens(newUser);
    }

    public TokenModel login(LoginModel user) {
        var currentUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new BadRequestException("Неверный email или пароль"));

        if (!passwordEncoder.matches(user.getPassword(), currentUser.getPasswordHash())) {
            throw new BadRequestException("Неверный email или пароль");
        }

        return createTokens(currentUser);
    }

    public void logout(LogoutModel logoutData, String email) {
       refreshTokenService.revokeToken(logoutData.getToken(), email);
    }

    public TokenModel refresh(TokenRefreshModel token) {
        var user = refreshTokenService.revokeTokenAndReturnUser(token.getToken());

        return createTokens(user);
    }

    private TokenModel createTokens(UserEntity user) {
        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createToken(user);

        return new TokenModel()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }
}
