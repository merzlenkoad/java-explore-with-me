package ru.practicum.main.comment.service;

import ru.practicum.main.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addCommentByUser(CommentDto commentDto);

    void deleteCommentByIdByUser(Long userId, Long commentId);

    CommentDto updateCommentByUser(Long commentId, CommentDto commentDto);

    List<CommentDto> getComments(Long eventId, Integer from, Integer size);

    CommentDto getCommentById(Long commentId);

    void deleteCommentByIdAdmin(Long userId, Long commentId);
}