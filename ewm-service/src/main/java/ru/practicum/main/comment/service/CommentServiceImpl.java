package ru.practicum.main.comment.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.CommentWithFullAuthorDto;
import ru.practicum.main.comment.mapper.CommentMapper;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.repository.CommentRepository;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.service.EventService;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.constant.Constants.timeNow;

@Slf4j
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventService eventService;

    @Transactional
    @Override
    public CommentDto addCommentForUser(CommentDto commentDto) {
        User user = userService.getUserIfExist(commentDto.getAuthor());
        Event event = eventService.getEventIfExist(commentDto.getEvent());
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(commentDto, user, event)));
    }

    @Transactional
    @Override
    public CommentDto updateCommentForUser(Long commentId, CommentDto commentDto) {
        User user = userService.getUserIfExist(commentDto.getAuthor());
        Event event = eventService.getEventIfExist(commentDto.getEvent());

        if (!commentRepository.getCommentById(commentId).getAuthor().getId().equals(commentDto.getAuthor())) {
            throw new NotFoundException("The user has no comment.");
        }

        Comment actualComment = CommentMapper.toComment(commentDto, user, event);
        Comment comment = getCommentIfExist(commentId);
        actualComment.setId(comment.getId());

        if (actualComment.getText() == null || actualComment.getText().isBlank()) {
            actualComment.setText(comment.getText());
        } else {
            actualComment.setText(actualComment.getText());
        }

        if (actualComment.getCreatedOn() == null) {
            actualComment.setCreatedOn(comment.getCreatedOn());
        } else {
            actualComment.setCreatedOn(actualComment.getCreatedOn());
        }

        actualComment.setUpdatedOn(timeNow());

        return CommentMapper.toCommentDto(commentRepository.save(actualComment));
    }


    @Transactional
    @Override
    public List<CommentDto> getCommentsForUser(Long eventId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<CommentDto> commentsDto = commentRepository.getCommentsByEventId(eventId, pageable).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        if (commentsDto.isEmpty()) {
            throw new NotFoundException("The event has no comments.");
        } else {
            return commentsDto;
        }
    }

    @Transactional
    @Override
    public CommentDto getCommentByIdForUser(Long commentId) {
        return CommentMapper.toCommentDto(getCommentIfExist(commentId));
    }

    @Transactional
    @Override
    public void deleteCommentByIdForUser(Long userId, Long commentId) {
        Comment comment = getCommentIfExist(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("The user has no comments.");
        } else {
            commentRepository.deleteById(commentId);
        }
    }

    @Transactional
    @Override
    public List<CommentWithFullAuthorDto> getCommentsForAdmin(Long eventId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<CommentDto> commentsDto = commentRepository.getCommentsByEventId(eventId, pageable).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        if (commentsDto.isEmpty()) {
            throw new NotFoundException("The event has no comments.");
        } else {
            return commentsDto.stream()
                    .map((CommentDto commentDto) -> CommentMapper.toCommentWithFullAuthorDto(commentDto,
                            userService.getUserIfExist(commentDto.getAuthor())))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    @Override
    public CommentWithFullAuthorDto getCommentByIdForAdmin(Long commentId) {
        CommentDto commentDto = CommentMapper.toCommentDto(getCommentIfExist(commentId));
        return CommentMapper.toCommentWithFullAuthorDto(commentDto, userService.getUserIfExist(commentDto.getAuthor()));
    }

    @Transactional
    @Override
    public void deleteCommentByIdForAdmin(Long userId, Long commentId) {
        getCommentIfExist(commentId);
        commentRepository.deleteById(commentId);
    }


    public Comment getCommentIfExist(Long commentId) {
        Comment comment = commentRepository.getCommentById(commentId);
        if (comment == null) {
            throw new NotFoundException("Comment is not exist.");
        } else {
            return comment;
        }
    }
}