package ru.hits.just_4sport.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.just_4sport.model.api.TokenModel;
import ru.hits.just_4sport.model.api.user.UserLoginModel;
import ru.hits.just_4sport.model.api.user.UserLogoutModel;
import ru.hits.just_4sport.model.api.user.UserRegistrationModel;
import ru.hits.just_4sport.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<TokenModel> registration(@RequestBody UserRegistrationModel user) {
        return ResponseEntity.ok(authService.registration(user));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenModel> login(@RequestBody UserLoginModel user) {
        return ResponseEntity.ok(authService.login(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody UserLogoutModel logoutData) {
        authService.logout(logoutData);

        return ResponseEntity.ok().build();
    }
}
