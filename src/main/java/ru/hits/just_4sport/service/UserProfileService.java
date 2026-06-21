package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.user.UserProfileModel;
import ru.hits.just_4sport.model.api.user.UserUpdateProfileModel;
import ru.hits.just_4sport.model.mapper.EventMapper;
import ru.hits.just_4sport.model.mapper.PhotoMapper;
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
    private final PhotoMapper photoMapper;
    private final PhotoService photoService;
    private final EventMapper eventMapper;

    public UserProfileModel getUserProfile(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        var profile = new UserProfileModel()
                .setName(user.getName())
                .setNickname(user.getNickname())
                .setEmail(user.getEmail())
                .setPhoto(photoMapper.toModel(user.getPhoto()))
                .setFavoriteSports(user.getFavoriteSports());

        var participantEvents = eventsRepository.findEventEntitiesByAnyTeamIn(user.getId())
                .stream()
                .map(eventMapper::toShortModel)
                .toList();

        var authorEvents = user.getAuthorEvents().stream()
                        .map(eventMapper::toShortModel)
                                .toList();

        profile.setParticipantEvents(participantEvents);
        profile.setAuthorEvents(authorEvents);

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

    public void setUserProfilePhoto(String email, MultipartFile photo) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        photoService.validateImage(photo);

        var oldPhoto = user.getPhoto();

        var photoEntity = photoService.buildPhotoEntity(photo, oldPhoto);

        user.setPhoto(photoEntity);
        userRepository.save(user);
    }

    public void deleteUserProfilePhoto(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        var photo = user.getPhoto();

        if (photo ==  null) {
            return;
        }

        photoService.deleteImage(photo.getPath());

        photoRepository.delete(photo);

        user.setPhoto(null);
        userRepository.save(user);
    }
}
