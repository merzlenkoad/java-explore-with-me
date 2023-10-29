package ru.practicum.main.event.mapper;


import ru.practicum.main.category.mapper.CategoryMapper;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.dto.NewEventDto;
import ru.practicum.main.event.dto.UpdateEventAdminRequest;
import ru.practicum.main.event.dto.UpdateEventUserRequest;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.State;
import ru.practicum.main.location.mapper.LocationMapper;
import ru.practicum.main.user.mapper.UserMapper;
import ru.practicum.main.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.main.constant.Constants.DATE_FORMAT;

public class EventMapper {
    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .requestModeration(event.getRequestModeration())
                .state(event.getState() == null ? null : event.getState().toString())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto, User initiator, Category category) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .initiator(initiator)
                .location(newEventDto.getLocation())
                .confirmedRequests(0L)
                .paid(newEventDto.getPaid() == null ? false : newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit() == null ? 0 : newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration() == null ? true : newEventDto.getRequestModeration())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .views(0L)
                .build();
    }

    public static Event toEvent(UpdateEventAdminRequest updateEventAdminRequest, Event oldEvent, Category category) {
        return Event.builder()
                .id(oldEvent.getId())
                .annotation(updateEventAdminRequest.getAnnotation() == null ? oldEvent.getAnnotation() : updateEventAdminRequest.getAnnotation())
                .category(updateEventAdminRequest.getCategory() == null ? oldEvent.getCategory() : category)
                .confirmedRequests(oldEvent.getConfirmedRequests())
                .createdOn(oldEvent.getCreatedOn())
                .description(updateEventAdminRequest.getDescription() == null ? oldEvent.getDescription() : updateEventAdminRequest.getDescription())
                .eventDate(updateEventAdminRequest.getEventDate() == null ? oldEvent.getEventDate() : LocalDateTime.parse(updateEventAdminRequest.getEventDate(), DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .initiator(oldEvent.getInitiator())
                .location(updateEventAdminRequest.getLocation() == null ? oldEvent.getLocation() : updateEventAdminRequest.getLocation())
                .paid(updateEventAdminRequest.getPaid() == null ? oldEvent.getPaid() : updateEventAdminRequest.getPaid())
                .participantLimit(updateEventAdminRequest.getParticipantLimit() == null ? oldEvent.getParticipantLimit() : updateEventAdminRequest.getParticipantLimit())
                .requestModeration(updateEventAdminRequest.getRequestModeration() == null ? oldEvent.getRequestModeration() : updateEventAdminRequest.getRequestModeration())
                .state(oldEvent.getState())
                .title(updateEventAdminRequest.getTitle() == null ? oldEvent.getTitle() : updateEventAdminRequest.getTitle())
                .views(oldEvent.getViews())
                .build();
    }

    public static Event toEvent(UpdateEventUserRequest updateEventUserRequest, Event oldEvent, Category category) {
        return Event.builder()
                .id(oldEvent.getId())
                .annotation(updateEventUserRequest.getAnnotation() == null ? oldEvent.getAnnotation() : updateEventUserRequest.getAnnotation())
                .category(updateEventUserRequest.getCategory() == null ? oldEvent.getCategory() : category)
                .confirmedRequests(oldEvent.getConfirmedRequests())
                .createdOn(oldEvent.getCreatedOn())
                .description(updateEventUserRequest.getDescription() == null ? oldEvent.getDescription() : updateEventUserRequest.getDescription())
                .eventDate(updateEventUserRequest.getEventDate() == null ? oldEvent.getEventDate() : LocalDateTime.parse(updateEventUserRequest.getEventDate(), DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .initiator(oldEvent.getInitiator())
                .location(updateEventUserRequest.getLocation() == null ? oldEvent.getLocation() : updateEventUserRequest.getLocation())
                .paid(updateEventUserRequest.getPaid() == null ? oldEvent.getPaid() : updateEventUserRequest.getPaid())
                .participantLimit(updateEventUserRequest.getParticipantLimit() == null ? oldEvent.getParticipantLimit() : updateEventUserRequest.getParticipantLimit())
                .requestModeration(updateEventUserRequest.getRequestModeration() == null ? oldEvent.getRequestModeration() : updateEventUserRequest.getRequestModeration())
                .title(updateEventUserRequest.getTitle() == null ? oldEvent.getTitle() : updateEventUserRequest.getTitle())
                .views(oldEvent.getViews())
                .build();
    }

}