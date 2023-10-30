package ru.practicum.main.comment.dto;

import lombok.*;

@Data
@Builder
public class CommentDto {

    private Long id;
    private String text;
    private Long event;
    private Long author;
    private String created;
}
