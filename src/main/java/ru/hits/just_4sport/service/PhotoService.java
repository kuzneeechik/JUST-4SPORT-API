package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.properties.UploadProperties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final UploadProperties uploadProperties;

    public Resource getPhoto(String photoName) {
        try {
            Path path = Path.of(uploadProperties.profilePhotosDir()).resolve(photoName).normalize();

            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                throw new NotFoundException("Фото не найдено");
            }

            return resource;
        } catch (MalformedURLException exception) {
            throw new NotFoundException("Фото не найдено");
        }
    }

    public String saveImage(MultipartFile photo) {
        try {
            Path path = Path.of(uploadProperties.profilePhotosDir());

            Files.createDirectories(path);

            var originalName = photo.getOriginalFilename();
            var extension = getExtension(originalName);
            var storedFileName = UUID.randomUUID() + extension;

            Path currentPath = path.resolve(storedFileName);

            Files.copy(photo.getInputStream(), currentPath, StandardCopyOption.REPLACE_EXISTING);

            return storedFileName;
        } catch (IOException e) {
            throw new BadRequestException("Не удалось сохранить файл");
        }
    }

    public void deleteImage(String path) {
        if (path == null || path.isBlank()) {
            return;
        }

        try {
            Path imagePath = Path.of(path).normalize();

            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            throw new BadRequestException("Не удалось удалить старое фото");
        }
    }

    public void validateImage(MultipartFile photo) {
        if (photo.isEmpty()) {
            throw new BadRequestException("Файл не должен быть пустым");
        }

        if (!List.of("image/jpeg", "image/jpg", "image/png").contains(photo.getContentType())) {
            throw new BadRequestException("Можно загрузить только JPG, JPEG или PNG");
        }
    }

    public String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }

        return fileName.substring(fileName.lastIndexOf("."));
    }
}
