package ru.practicum.main.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.dto.CommentWithFullAuthorDto;
import ru.practicum.main.comment.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin")
public class CommentAdminController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public void deleteCommentByIdForAdmin(HttpServletRequest request,
                                @Positive @PathVariable Long userId,
                                @Positive @PathVariable Long commentId) {
        log.info("Delete request received: delete comment by admin.");
        commentService.deleteCommentByIdForAdmin(userId, commentId);
    }

    @GetMapping("/events/{eventId}/comments")
    public ResponseEntity<List<CommentWithFullAuthorDto>> getCommentsForAdmin(HttpServletRequest request,
                                                                              @PathVariable Long eventId,
                                                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get request received: get all comments for admin.");
        return new ResponseEntity<>(commentService.getCommentsForAdmin(eventId, from, size), HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentWithFullAuthorDto> getCommentByIdForAdmin(HttpServletRequest request,
                                                            @Positive @PathVariable Long commentId) {
        log.info("Get request received: get comment by id for admin.");
        return new ResponseEntity<>(commentService.getCommentByIdForAdmin(commentId), HttpStatus.OK);
    }
}