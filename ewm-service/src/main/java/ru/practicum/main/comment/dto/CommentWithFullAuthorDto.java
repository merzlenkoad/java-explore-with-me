package ru.practicum.main.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.main.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentWithFullAuthorDto {

    private Long id;
    private String text;
    private Long event;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private User author;
}
