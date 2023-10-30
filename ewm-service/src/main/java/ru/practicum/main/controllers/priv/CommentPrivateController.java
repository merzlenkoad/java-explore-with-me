package ru.practicum.main.controllers.priv;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
public class CommentPrivateController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CommentDto> addComment(HttpServletRequest request,
                                                @NonNull @RequestBody CommentDto commentDto) {
        log.info("Post request received: add comment.");
        return new ResponseEntity<>(commentService.addCommentByUser(commentDto), HttpStatus.CREATED);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(HttpServletRequest request,
                                           @Positive @PathVariable Long commentId,
                                           @Valid @RequestBody CommentDto commentDto) {
        log.info("Patch request received: update comment by user.");
        return new ResponseEntity<>(commentService.updateCommentByUser(commentId, commentDto), HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(HttpServletRequest request,
                                     @Positive @PathVariable Long commentId) {
        log.info("Get request received: get comment by id.");
        return new ResponseEntity<>(commentService.getCommentById(commentId), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(HttpServletRequest request,
                                                         @PathVariable Long eventId,
                                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get request received: get all comment by user.");
        return new ResponseEntity<>(commentService.getComments(eventId, from, size), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public void deleteCommentByUser(HttpServletRequest request,
                           @Positive @PathVariable Long userId,
                           @Positive @PathVariable Long commentId) {
        log.info("Delete request received: delete comment by user.");
        commentService.deleteCommentByIdByUser(userId, commentId);
    }
}