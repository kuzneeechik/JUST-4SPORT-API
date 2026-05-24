package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.user.UserProfileModel;
import ru.hits.just_4sport.repository.EventsRepository;
import ru.hits.just_4sport.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;

    public UserProfileModel getUserProfile(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        var profile = new UserProfileModel()
                .setName(user.getName())
                .setNickname(user.getNickname())
                .setEmail(user.getEmail())
                .setPhoto(user.getPhoto())
                .setFavoriteSports(user.getFavoriteSports())
                .setAuthorEvents(user.getAuthorEvents());

        var participantEvent = eventsRepository.findEventEntitiesByTeams(user.getTeams());

        profile.setParticipantEvents(participantEvent);

        return profile;
    }
}
