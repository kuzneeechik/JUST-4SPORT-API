package ru.hits.just_4sport.model.api.comment;

import lombok.Data;

import java.util.UUID;

@Data
public class CommentCreateModel {

    private String content;

    private UUID parentId;
}
