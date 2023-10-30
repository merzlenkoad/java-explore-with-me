package ru.practicum.main.comment.mapper;

import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.main.constant.Constants.DATE_FORMAT;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .event(comment.getEvent().getId())
                .author(comment.getAuthor().getId())
                .created(comment.getCreated().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .build();
    }

    public static Comment toComment(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .text(commentDto.getText())
                .event(event)
                .author(user)
                .created(LocalDateTime.now())
                .build();
    }
}