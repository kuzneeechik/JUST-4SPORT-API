package ru.hits.just_4sport.model.mapper;

import org.mapstruct.Mapper;
import ru.hits.just_4sport.model.api.comment.CommentModel;
import ru.hits.just_4sport.model.domain.CommentEntity;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    default CommentModel toModel(CommentEntity comment) {
        if (comment == null) {
            return null;
        }

        var parentId = comment.getParent() == null ? null : comment.getParent().getId();

        return new CommentModel()
                .setId(comment.getId())
                .setContent(comment.getContent())
                .setParentId(parentId);
    }
}
