package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.user.UserPhotoModel;
import ru.hits.just_4sport.model.api.user.UserProfileModel;
import ru.hits.just_4sport.model.api.user.UserUpdateProfileModel;
import ru.hits.just_4sport.model.domain.PhotoEntity;
import ru.hits.just_4sport.repository.EventsRepository;
import ru.hits.just_4sport.repository.PhotoRepository;
import ru.hits.just_4sport.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;
    private final PhotoRepository photoRepository;

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

    public void updateUserProfile(UUID id, UserUpdateProfileModel userProfile) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.setName(userProfile.getName())
                .setNickname(userProfile.getNickname())
                .setEmail(userProfile.getEmail())
                .setFavoriteSports(userProfile.getFavoriteSports());

        userRepository.save(user);
    }

    public void deleteUserProfile(UUID id) {
        userRepository.deleteById(id);
    }

    public void setUserProfilePhoto(UUID id, UserPhotoModel photo) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        var newPhoto = new PhotoEntity()
                .setTitle(photo.getTitle())
                .setPath(photo.getPath());

        photoRepository.save(newPhoto);

        user.setPhoto(newPhoto);

        userRepository.save(user);
    }

    public void deleteUserProfilePhoto(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        var photo = user.getPhoto();

        photoRepository.delete(photo);

        user.setPhoto(null);

        userRepository.save(user);
    }
}
