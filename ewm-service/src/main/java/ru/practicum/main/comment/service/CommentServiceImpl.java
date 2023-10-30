package ru.practicum.main.comment.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.mapper.CommentMapper;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.repository.CommentRepository;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public CommentDto addCommentByUser(CommentDto commentDto) {
        User user = getUserIfExist(commentDto.getAuthor());
        Event event = getEventIfExist(commentDto.getEvent());
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(commentDto, user, event)));
    }

    @Transactional
    @Override
    public CommentDto updateCommentByUser(Long commentId, CommentDto commentDto) {
        getUserIfExist(commentDto.getAuthor());
        getEventIfExist(commentDto.getEvent());

        if (!commentRepository.getCommentById(commentId).getAuthor().getId().equals(commentDto.getAuthor())) {
            throw new NotFoundException("The user has no comment.");
        }

        Comment comment = getCommentIfExist(commentId);

        Event event = eventRepository.getEventsById(comment.getEvent().getId());
        User user = userRepository.getUserById(comment.getAuthor().getId());

        Comment actualComment = CommentMapper.toComment(commentDto, user, event);

        actualComment.setId(comment.getId());

        if (actualComment.getText() == null || actualComment.getText().isBlank()) {
            actualComment.setText(comment.getText());
        } else {
            actualComment.setText(actualComment.getText());
        }

        if (actualComment.getCreated() == null) {
            actualComment.setCreated(comment.getCreated());
        } else {
            actualComment.setCreated(actualComment.getCreated());
        }

        return CommentMapper.toCommentDto(commentRepository.save(actualComment));
    }


    @Transactional
    @Override
    public List<CommentDto> getComments(Long eventId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<CommentDto> commentsDto = commentRepository.getCommentByEventId(eventId, pageable).stream()
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
    public CommentDto getCommentById(Long commentId) {
        return CommentMapper.toCommentDto(getCommentIfExist(commentId));
    }

    @Transactional
    @Override
    public void deleteCommentByIdByUser(Long userId, Long commentId) {
        Comment comment = getCommentIfExist(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("The user has no comments.");
        } else {
            commentRepository.deleteById(commentId);
        }
    }

    @Transactional
    @Override
    public void deleteCommentByIdAdmin(Long userId, Long commentId) {
        getCommentIfExist(commentId);
        commentRepository.deleteById(commentId);
    }

    private Comment getCommentIfExist(Long commentId) {
        Comment comment = commentRepository.getCommentById(commentId);
        if (comment == null) {
            throw new NotFoundException("Comment is not exist.");
        } else {
            return comment;
        }
    }

    private Event getEventIfExist(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event == null) {
            throw new NotFoundException("Event is not exist.");
        } else {
            return event.get();
        }
    }

    private User getUserIfExist(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User is not exist.");
        } else {
            return user.get();
        }
    }
}