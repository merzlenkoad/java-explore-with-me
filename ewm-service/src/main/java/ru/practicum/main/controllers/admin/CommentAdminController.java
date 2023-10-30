package ru.practicum.main.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.comment.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin")
public class CommentAdminController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public void deleteCommentByAdmin(HttpServletRequest request,
                                @Positive @PathVariable Long userId,
                                @Positive @PathVariable Long commentId) {
        log.info("Delete request received: delete comment by admin.");
        commentService.deleteCommentByIdAdmin(userId, commentId);
    }
}