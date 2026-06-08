package ru.hits.just_4sport.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.hits.just_4sport.model.api.IdModel;
import ru.hits.just_4sport.model.api.comment.CommentCreateModel;
import ru.hits.just_4sport.model.api.comment.CommentEditModel;
import ru.hits.just_4sport.service.CommentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{eventId}")
    public ResponseEntity<IdModel> addComment(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID eventId,
            @RequestBody CommentCreateModel comment
    ) {
        return ResponseEntity.ok(commentService.addComment(
                user.getUsername(),
                eventId,
                comment
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editComment(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id,
            @RequestBody CommentEditModel comment
    ) {
        commentService.editComment(
                user.getUsername(),
                id,
                comment
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id
    ) {
        commentService.deleteComment(user.getUsername(), id);

        return ResponseEntity.ok().build();
    }
}
