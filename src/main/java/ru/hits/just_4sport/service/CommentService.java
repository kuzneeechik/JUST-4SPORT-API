package ru.hits.just_4sport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.IdModel;
import ru.hits.just_4sport.model.api.comment.CommentCreateModel;
import ru.hits.just_4sport.model.api.comment.CommentEditModel;
import ru.hits.just_4sport.model.domain.CommentEntity;
import ru.hits.just_4sport.repository.CommentRepository;
import ru.hits.just_4sport.repository.EventRepository;
import ru.hits.just_4sport.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    public IdModel addComment(
            String email,
            UUID eventId,
            CommentCreateModel comment
    ) {
        var author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Автор не найден"));

        var event = eventRepository.findEventEntitiesById(eventId)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        var parentComment = commentRepository.findCommentEntityById(comment.getParentId())
                .orElseThrow(() -> new NotFoundException("Родительский комментарий не найден"));

        var newComment = new CommentEntity()
                .setContent(comment.getContent())
                .setEvent(event)
                .setAuthor(author)
                .setParent(parentComment);

        event.getComments().add(newComment);

        commentRepository.save(newComment);

        return new IdModel().setId(newComment.getId());
    }

    public void editComment(
            String email,
            UUID commentId,
            CommentEditModel comment
    ) {
        var commentEntity = commentRepository.findCommentEntityById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        var author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Автор не найден"));

        if (author != commentEntity.getAuthor()) {
            throw new BadRequestException("Текущий пользователь не может редактировать этот комментарий");
        }

        commentEntity.setContent(comment.getContent());

        commentRepository.save(commentEntity);
    }
}
