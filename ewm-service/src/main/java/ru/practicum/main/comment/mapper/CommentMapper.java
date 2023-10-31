package ru.practicum.main.comment.mapper;

import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.CommentWithFullAuthorDto;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.user.model.User;

import static ru.practicum.main.constant.Constants.timeNow;

public class CommentMapper {

    public static CommentWithFullAuthorDto toCommentWithFullAuthorDto(CommentDto commentDto, User user) {
        return CommentWithFullAuthorDto.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .event(commentDto.getEvent())
                .createdOn(commentDto.getCreatedOn())
                .updatedOn(commentDto.getUpdatedOn())
                .author(user)
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .event(comment.getEvent().getId())
                .author(comment.getAuthor().getId())
                .createdOn(comment.getCreatedOn())
                .updatedOn(comment.getUpdatedOn())
                .build();
    }

    public static Comment toComment(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .text(commentDto.getText())
                .event(event)
                .author(user)
                .createdOn(timeNow())
                .build();
    }
}