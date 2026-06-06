package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.user.UserPhotoModel;
import ru.hits.just_4sport.model.api.user.UserProfileModel;
import ru.hits.just_4sport.model.api.user.UserUpdateProfileModel;
import ru.hits.just_4sport.model.domain.PhotoEntity;
import ru.hits.just_4sport.repository.EventRepository;
import ru.hits.just_4sport.repository.PhotoRepository;
import ru.hits.just_4sport.repository.RefreshTokenRepository;
import ru.hits.just_4sport.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final EventRepository eventsRepository;
    private final PhotoRepository photoRepository;
    private final RefreshTokenRepository refreshTokenRepository;

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

        var participantEvents = eventsRepository.findEventEntitiesByAnyTeamIn(user.getTeams());

        profile.setParticipantEvents(participantEvents);

        return profile;
    }

    public UserProfileModel getMyProfile(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return getUserProfile(user.getId());
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

    @Transactional
    public void deleteUserProfile(UUID id) {
        refreshTokenRepository.deleteAllByUserId(id);

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
