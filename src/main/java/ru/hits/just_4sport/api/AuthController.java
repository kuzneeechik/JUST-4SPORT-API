package ru.hits.just_4sport.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.just_4sport.model.api.auth.*;
import ru.hits.just_4sport.service.auth.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<TokenModel> registration(@RequestBody RegistrationModel user) {
        return ResponseEntity.ok(authService.registration(user));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenModel> login(@RequestBody LoginModel user) {
        return ResponseEntity.ok(authService.login(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody LogoutModel logoutData,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        authService.logout(logoutData, userDetails.getUsername());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenModel> refresh(@RequestBody TokenRefreshModel token) {
        return ResponseEntity.ok(authService.refresh(token));
    }
}
