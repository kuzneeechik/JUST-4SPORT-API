package ru.hits.just_4sport.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.just_4sport.model.api.TokenModel;
import ru.hits.just_4sport.model.api.user.UserRegistrationModel;
import ru.hits.just_4sport.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<TokenModel> registration(@RequestBody UserRegistrationModel user) {
        return ResponseEntity.ok(userService.registration(user));
    }
}
