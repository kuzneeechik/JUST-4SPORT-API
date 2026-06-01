package ru.hits.just_4sport.model.mapper;

import org.mapstruct.Mapper;
import ru.hits.just_4sport.model.api.CommentModel;
import ru.hits.just_4sport.model.domain.CommentEntity;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    default CommentModel toModel(CommentEntity comment) {
        if (comment == null) {
            return null;
        }

        return new CommentModel()
                .setId(comment.getId())
                .setContent(comment.getContent())
                .setAuthorName(comment.getAuthor().getName())
                .setParent(comment.getParent().getId());
    }
}
