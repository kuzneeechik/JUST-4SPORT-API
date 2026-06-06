package ru.hits.just_4sport.model.api;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

@Data
@Accessors(chain = true)
public class FileModel {

    private Resource resource;

    private MediaType mediaType;
}
