package ru.practicum.main.comment.service;

import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.CommentWithFullAuthorDto;

import java.util.List;

public interface CommentService {
    CommentDto addCommentForUser(CommentDto commentDto);

    CommentDto updateCommentForUser(Long commentId, CommentDto commentDto);

    List<CommentDto> getCommentsForUser(Long eventId, Integer from, Integer size);

    CommentDto getCommentByIdForUser(Long commentId);

    void deleteCommentByIdForUser(Long userId, Long commentId);

    List<CommentWithFullAuthorDto> getCommentsForAdmin(Long eventId, Integer from, Integer size);

    CommentWithFullAuthorDto getCommentByIdForAdmin(Long commentId);

    void deleteCommentByIdForAdmin(Long userId, Long commentId);
}