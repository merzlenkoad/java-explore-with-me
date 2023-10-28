package ru.practicum.main.event.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.StatsClient;
import ru.practicum.statsDto.EndpointHitDto;
import ru.practicum.statsDto.ViewStats;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.dto.NewEventDto;
import ru.practicum.main.event.dto.UpdateEventAdminRequest;
import ru.practicum.main.event.dto.UpdateEventUserRequest;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.State;
import ru.practicum.main.event.model.Status;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exception.EventDateException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.OverflowLimitException;
import ru.practicum.main.exception.StateArgumentException;
import ru.practicum.main.exception.StatusPerticipationRequestException;
import ru.practicum.main.location.model.Location;
import ru.practicum.main.location.repository.LocationRepository;
import ru.practicum.main.participation.mapper.ParticipationMapper;
import ru.practicum.main.participation.dto.ParticipationRequestDto;
import ru.practicum.main.participation.model.ParticipationRequest;
import ru.practicum.main.participation.repository.ParticipationRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.constant.Constants.DATE_FORMAT;

@Slf4j
@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;
    private final LocationRepository locationRepository;
    private final StatsClient statsClient;

    @Transactional
    @Override
    public List<EventShortDto> getEventsPrivate(Long userId, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.getEventsByInitiatorId(userId, pageable)
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto addEventPrivate(Long userId, NewEventDto newEventDto) {
        LocalDateTime start = LocalDateTime.parse(newEventDto.getEventDate(),
                DateTimeFormatter.ofPattern(DATE_FORMAT));

        if (start.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("The start time is specified incorrectly");
        }
        Location location = locationRepository.save(newEventDto.getLocation());
        newEventDto.setLocation(location);
        Category category = categoryRepository.getById(newEventDto.getCategory());
        User user = userRepository.getUserById(userId);

        Event event = EventMapper.toEvent(newEventDto, user, category);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Transactional
    @Override
    public EventFullDto getEventPrivate(Long userId, Long eventId) {

        Event event = eventRepository.getEventsByIdAndInitiatorId(eventId, userId);
        if (event == null) {
            throw new NotFoundException("The event not found.");
        }
        return EventMapper.toEventFullDto(event);
    }


    @Transactional
    @Override
    public EventFullDto updateEventPrivate(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event oldEvent = eventRepository.getEventsByIdAndInitiatorId(eventId, userId);

        validateUpdateEventPrivate(oldEvent, updateEventUserRequest);

        if (updateEventUserRequest.getLocation() != null) {
            Location location = locationRepository.save(updateEventUserRequest.getLocation());
            updateEventUserRequest.setLocation(location);
        }

        Category newCategory = updateEventUserRequest.getCategory() == null ?
                oldEvent.getCategory() : categoryRepository.getById(updateEventUserRequest.getCategory());

        Event upEvent = oldEvent;
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals("SEND_TO_REVIEW")) {
                upEvent = EventMapper.toEvent(updateEventUserRequest, oldEvent, newCategory);
                upEvent.setState(State.PENDING);
            }
            if (updateEventUserRequest.getStateAction().equals("CANCEL_REVIEW")) {
                upEvent.setState(State.CANCELED);

            }
        }

        upEvent.setId(eventId);

        return EventMapper.toEventFullDto(eventRepository.save(upEvent));
    }

    private void validateUpdateEventPrivate(Event oldEvent, UpdateEventUserRequest updateEventUserRequest) {
        if (oldEvent == null) {
            throw new NotFoundException("The event not found.");
        }

        if (oldEvent.getState() != null && oldEvent.getState().equals(State.PUBLISHED)) {
            throw new StateArgumentException("Cannot cancel events that are not pending or not canceled");
        }

        LocalDateTime start = oldEvent.getEventDate();
        if (updateEventUserRequest.getEventDate() != null) {
            if (LocalDateTime.parse(updateEventUserRequest.getEventDate(), DateTimeFormatter.ofPattern(DATE_FORMAT))
                    .isBefore(start.plusHours(2))) {
                throw new IllegalArgumentException("The start time is before or equals eventDate");
            }
        }
    }

    @Transactional
    @Override
    public List<ParticipationRequestDto> getRequestsEventsUserPrivate(Long userId, Long eventId) {
        return participationRepository.getParticipationRequestsByEvent(eventId)
                .stream()
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateEventRequestStatusPrivate(Long userId,
                                                                          Long eventId,
                                                                          EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        Event event = eventRepository.getEventsByIdAndInitiatorId(eventId, userId);
        if (event == null) {
            throw new NotFoundException("The event not found.");
        }
        if (Long.valueOf(event.getParticipantLimit()).equals(event.getConfirmedRequests())) {
            throw new OverflowLimitException("Cannot exceed the number of participants.");
        }
        Status status = Status.valueOf(eventRequestStatusUpdateRequest.getStatus());

        List<ParticipationRequest> list = participationRepository.getParticipationRequestByIdIn(eventRequestStatusUpdateRequest.getRequestIds());

        List<ParticipationRequest> listPending = new ArrayList<>();
        List<ParticipationRequest> listRejected = new ArrayList<>();
        List<ParticipationRequest> listOld = new ArrayList<>();
        List<ParticipationRequestDto> listDto = new ArrayList<>();
        List<ParticipationRequestDto> listDtoReject = new ArrayList<>();

        if (event.getParticipantLimit() == 0 && !event.getRequestModeration()) {
            return new EventRequestStatusUpdateResult(listDto, listDtoReject);
        } else if (event.getParticipantLimit() > 0 && !event.getRequestModeration()) {
            for (ParticipationRequest participationRequest : list) {
                if (!participationRequest.getStatus().equals(Status.PENDING)) {
                    throw new StatusPerticipationRequestException("Wrong status request");
                }
                if (status.equals(Status.CONFIRMED)) {
                    listOld.add(participationRequest);

                    participationRequest.setStatus(Status.CONFIRMED);
                    listPending.add(participationRequest);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    participationRepository.saveAndFlush(participationRequest);

                    if (Long.valueOf(event.getParticipantLimit()).equals(event.getConfirmedRequests())) {
                        list.removeAll(listOld);
                        if (list.size() != 0) {
                            listDto = listPending.stream().map(ParticipationMapper::toParticipationRequestDto).collect(Collectors.toList());
                            listDtoReject = list.stream().map(ParticipationMapper::toParticipationRequestDto).collect(Collectors.toList());
                            return new EventRequestStatusUpdateResult(listDto, listDtoReject);
                        } else {
                            listDto = listPending.stream().map(ParticipationMapper::toParticipationRequestDto).collect(Collectors.toList());
                            return new EventRequestStatusUpdateResult(listDto, new ArrayList<>());
                        }
                    }
                } else {
                    participationRequest.setStatus(Status.REJECTED);
                    listRejected.add(participationRequest);
                    participationRepository.saveAndFlush(participationRequest);
                    listDtoReject = list.stream().map(ParticipationMapper::toParticipationRequestDto).collect(Collectors.toList());
                    return new EventRequestStatusUpdateResult(new ArrayList<>(), listDtoReject);
                }
            }
            listDto = listPending.stream().map(ParticipationMapper::toParticipationRequestDto).collect(Collectors.toList());
            return new EventRequestStatusUpdateResult(listDto, new ArrayList<>());
        } else if (event.getParticipantLimit() > 0 && event.getRequestModeration()) {
            for (ParticipationRequest participationRequest : list) {
                if (!participationRequest.getStatus().equals(Status.PENDING)) {
                    throw new StatusPerticipationRequestException("Wrong status request.");
                }
                if (status.equals(Status.CONFIRMED)) {
                    listOld.add(participationRequest);

                    participationRequest.setStatus(Status.CONFIRMED);
                    listPending.add(participationRequest);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    participationRepository.saveAndFlush(participationRequest);

                    if (Long.valueOf(event.getParticipantLimit()).equals(event.getConfirmedRequests())) {
                        list.removeAll(listOld);
                        if (list.size() != 0) {
                            listDto = listPending.stream().map(ParticipationMapper::toParticipationRequestDto).collect(Collectors.toList());
                            listDtoReject = list.stream().map(ParticipationMapper::toParticipationRequestDto).collect(Collectors.toList());
                            return new EventRequestStatusUpdateResult(listDto, listDtoReject);
                        } else {
                            listDto = listPending.stream().map(ParticipationMapper::toParticipationRequestDto).collect(Collectors.toList());
                            return new EventRequestStatusUpdateResult(listDto, new ArrayList<>());
                        }
                    }
                } else {
                    participationRequest.setStatus(Status.REJECTED);
                    listRejected.add(participationRequest);
                    participationRepository.saveAndFlush(participationRequest);
                    listDtoReject = list.stream().map(ParticipationMapper::toParticipationRequestDto).collect(Collectors.toList());
                    return new EventRequestStatusUpdateResult(new ArrayList<>(), listDtoReject);
                }
            }
        }
        listDto = listPending.stream().map(ParticipationMapper::toParticipationRequestDto).collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(listDto, new ArrayList<>());

    }


    @Transactional
    @Override
    public List<EventFullDto> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                             String rangeStart, String rangeEnd, Integer from, Integer size) {

        List<EventFullDto> list;
        List<State> stateEnum = null;
        if (states != null) {
            stateEnum = states.stream().map(State::valueOf).collect(Collectors.toList());
        }
        Pageable pageable = PageRequest.of(from / size, size);
        if (rangeStart == null && rangeEnd == null) {
            if (users == null && states == null && categories == null) {
                list = eventRepository.findAll(pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());
            } else if (users == null && states == null) {
                list = eventRepository.getEventsByCategoryIdIn(categories, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users == null && categories == null) {
                list = eventRepository.getEventsByStateIn(stateEnum, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users != null && states == null && categories == null) {
                list = eventRepository.getEventsByInitiatorIdIn(users, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users == null) {
                list = eventRepository.getEventsByCategoryIdInAndStateIn(categories, stateEnum, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (states != null && categories == null) {
                list = eventRepository.getEventsByInitiatorIdInAndStateIn(users, stateEnum, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (states == null) {
                list = eventRepository.getEventsByInitiatorIdInAndCategoryIdIn(users, categories, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else {
                list = eventRepository.getEventsByInitiatorIdInAndStateInAndCategoryIdIn(users, stateEnum, categories, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());
            }
        } else {
            LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(DATE_FORMAT));
            LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(DATE_FORMAT));
            if (start.isAfter(end)) {
                throw new IllegalArgumentException();
            }
            if (users == null && states == null && categories == null) {
                list = eventRepository.getEventsByEventDateAfterAndEventDateBefore(start, end, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users == null && states == null) {
                list = eventRepository.getEventsByCategoryIdInAndEventDateAfterAndEventDateBefore(
                        categories, start, end, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users == null && categories == null) {
                list = eventRepository.getEventsByStateInAndEventDateAfterAndEventDateBefore(
                        stateEnum, start, end, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users != null && states == null && categories == null) {
                list = eventRepository.getEventsByInitiatorIdInAndEventDateAfterAndEventDateBefore(
                        users, start, end, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (users == null) {
                list = eventRepository.getEventsByStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
                        stateEnum, categories, start, end, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (states != null && categories == null) {
                list = eventRepository.getEventsByInitiatorIdInAndStateInAndEventDateAfterAndEventDateBefore(
                        users, stateEnum, start, end, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else if (states == null) {
                list = eventRepository.getEventsByInitiatorIdInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
                        users, categories, start, end, pageable).stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());

            } else list = eventRepository.getEventsByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
                    users, stateEnum, categories, start, end, pageable)
                    .stream()
                    .map(EventMapper::toEventFullDto)
                    .collect(Collectors.toList());
        }

        return list;
    }

    @Transactional
    @Override
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event oldEvent = eventRepository.getEventsById(eventId);

        validateUpdateEventAdmin(oldEvent, updateEventAdminRequest);

        if (updateEventAdminRequest.getLocation() != null) {
            Location location = locationRepository.save(updateEventAdminRequest.getLocation());
            updateEventAdminRequest.setLocation(location);
        }

        Category newCategory = updateEventAdminRequest.getCategory() == null ?
                oldEvent.getCategory() : categoryRepository.getById(updateEventAdminRequest.getCategory());

        Event upEvent = oldEvent;
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals("PUBLISH_EVENT")) {
                upEvent = EventMapper.toEvent(updateEventAdminRequest, oldEvent, newCategory);
                upEvent.setPublishedOn(LocalDateTime.now());
                upEvent.setState(State.PUBLISHED);
            }
            if (updateEventAdminRequest.getStateAction().equals("REJECT_EVENT")) {
                upEvent.setState(State.CANCELED);

            }
        }
        upEvent.setId(eventId);

        return EventMapper.toEventFullDto(eventRepository.save(upEvent));
    }


    private void validateUpdateEventAdmin(Event oldEvent, UpdateEventAdminRequest updateEventAdminRequest) {
        if (oldEvent == null) {
            throw new NotFoundException("Event not found.");
        }

        LocalDateTime start = oldEvent.getEventDate();
        if (oldEvent.getPublishedOn() != null && start.isAfter(oldEvent.getPublishedOn().plusHours(1))) {
            throw new EventDateException("Start time is before event date");
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime newEventDate = LocalDateTime.parse(updateEventAdminRequest.getEventDate(), DateTimeFormatter.ofPattern(DATE_FORMAT));
            LocalDateTime currentTime = LocalDateTime.now();
            if (newEventDate.isBefore(currentTime) || newEventDate.isEqual(currentTime)) {
                throw new IllegalArgumentException("Start time before or equals event date");
            }
        }

        if (oldEvent.getState() != null && !oldEvent.getState().equals(State.PENDING) && updateEventAdminRequest.getStateAction().equals("PUBLISH_EVENT")) {
            throw new StateArgumentException("Wrong state: PUBLISHED OR CANCELED");
        }
        if (oldEvent.getState() != null && oldEvent.getState().equals(State.PUBLISHED) && updateEventAdminRequest.getStateAction().equals("REJECT_EVENT")) {
            throw new StateArgumentException("Wrong state: PUBLISHED");
        }
    }

    @Transactional
    @Override
    public List<EventShortDto> getEventsAndStatsPublic(HttpServletRequest request, String
            text, List<Long> categories, Boolean paid,
                                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                       String sort, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        LocalDateTime timeNow = LocalDateTime.now();

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new IllegalArgumentException("Range start time is after range end time");
            }
        }

        List<Event> list;
        if (text != null) {
            text = "%" + text + "%";
        }
        if (paid != null) {
            if (rangeStart == null && rangeEnd == null) {
                if (sort != null && sort.equals("EVENT_DATE")) {
                    if (onlyAvailable) {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsNoPeriodSortEventDateAvailableCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    timeNow,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsNoPeriodSortEventDateAvailableCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    timeNow,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsNoPeriodSortEventDateAvailableText(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    timeNow,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsNoPeriodSortEventDateAvailable(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    timeNow,
                                    pageable);
                        }
                    } else {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsNoPeriodSortEventDateCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    timeNow,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsNoPeriodSortEventDateCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    timeNow,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsNoPeriodSortEventDateText(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    timeNow,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsNoPeriodSortEventDate(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    timeNow,
                                    pageable);
                        }
                    }
                } else if (sort != null && sort.equals("VIEWS")) {
                    if (onlyAvailable) {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsNoPeriodSortViewsAvailableCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    timeNow,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsNoPeriodSortViewsAvailableCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    timeNow,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsNoPeriodSortViewsAvailableText(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    timeNow,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsNoPeriodSortViewsAvailable(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    timeNow,
                                    pageable);
                        }
                    } else {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsNoPeriodSortViewsCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    timeNow,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsNoPeriodSortViewsCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    timeNow,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsNoPeriodSortViewsText(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    timeNow,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsNoPeriodSortViews(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    timeNow,
                                    pageable);
                        }

                    }
                } else {
                    if (onlyAvailable) {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsNoPeriodAvailableCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    timeNow,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsNoPeriodAvailableCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    timeNow,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsNoPeriodAvailableText(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    timeNow,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsNoPeriodAvailable(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    timeNow,
                                    pageable);
                        }
                    } else {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsNoPeriodCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    timeNow,
                                    text,
                                    pageable);
                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsNoPeriodCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    timeNow,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsNoPeriodText(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    timeNow,
                                    text,
                                    pageable);

                        } else {
                            list = eventRepository.getEventsNoPeriod(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    timeNow,
                                    pageable);
                        }
                    }
                }

            } else {
                if (sort != null && sort.equals("EVENT_DATE")) {
                    if (onlyAvailable) {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsPeriodSortEventDateAvailableCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsPeriodSortEventDateAvailableCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsPeriodSortEventDateAvailableText(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsPeriodSortEventDateAvailable(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);
                        }
                    } else {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsPeriodSortEventDateCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsPeriodSortEventDateCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsPeriodSortEventDateText(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsPeriodSortEventDate(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);
                        }
                    }
                } else if (sort != null && sort.equals("VIEWS")) {
                    if (onlyAvailable) {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsPeriodSortViewsAvailableCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsPeriodSortViewsAvailableCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsPeriodSortViewsAvailableText(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsPeriodSortViewsAvailable(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);
                        }
                    } else {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsPeriodSortViewsCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsPeriodSortViewsCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsPeriodSortViewsText(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsPeriodSortViews(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);
                        }
                    }
                } else {
                    if (onlyAvailable) {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsPeriodAvailableCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsPeriodAvailableCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsPeriodAvailableText(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsPeriodAvailable(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);
                        }

                    } else {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsPeriodCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsPeriodCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsPeriodText(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else {
                            list = eventRepository.getEventsPeriod(
                                    State.PUBLISHED.toString(),
                                    paid,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);
                        }
                    }
                }
            }
        } else {
            if (rangeStart == null && rangeEnd == null) {
                if (sort != null && sort.equals("EVENT_DATE")) {
                    if (onlyAvailable) {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsNoPeriodSortEventDateAvailableCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    timeNow,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsNoPeriodSortEventDateAvailableCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    timeNow,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsNoPeriodSortEventDateAvailableText(
                                    State.PUBLISHED.toString(),
                                    timeNow,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsNoPeriodSortEventDateAvailable(
                                    State.PUBLISHED.toString(),
                                    timeNow,
                                    pageable);
                        }
                    } else {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsNoPeriodSortEventDateCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    timeNow,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsNoPeriodSortEventDateCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    timeNow,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsNoPeriodSortEventDateText(
                                    State.PUBLISHED.toString(),
                                    timeNow,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsNoPeriodSortEventDate(
                                    State.PUBLISHED.toString(),
                                    timeNow,
                                    pageable);
                        }
                    }
                } else if (sort != null && sort.equals("VIEWS")) {
                    if (onlyAvailable) {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsNoPeriodSortViewsAvailableCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    timeNow,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsNoPeriodSortViewsAvailableCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    timeNow,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsNoPeriodSortViewsAvailableText(
                                    State.PUBLISHED.toString(),
                                    timeNow,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsNoPeriodSortViewsAvailable(
                                    State.PUBLISHED.toString(),
                                    timeNow,
                                    pageable);
                        }
                    } else {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsNoPeriodSortViewsCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    timeNow,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsNoPeriodSortViewsCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    timeNow,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsNoPeriodSortViewsText(
                                    State.PUBLISHED.toString(),
                                    timeNow,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsNoPeriodSortViews(
                                    State.PUBLISHED.toString(),
                                    timeNow,
                                    pageable);
                        }

                    }
                } else {
                    if (onlyAvailable) {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsNoPeriodAvailableCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    timeNow,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsNoPeriodAvailableCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    timeNow,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsNoPeriodAvailableText(
                                    State.PUBLISHED.toString(),
                                    timeNow,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsNoPeriodAvailable(
                                    State.PUBLISHED.toString(),
                                    timeNow,
                                    pageable);
                        }
                    } else {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsNoPeriodCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    timeNow,
                                    text,
                                    pageable);
                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsNoPeriodCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    timeNow,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsNoPeriodText(
                                    State.PUBLISHED.toString(),
                                    timeNow,
                                    text,
                                    pageable);

                        } else {
                            list = eventRepository.getEventsNoPeriod(
                                    State.PUBLISHED.toString(),
                                    timeNow,
                                    pageable);
                        }
                    }
                }

            } else {
                if (rangeStart.isAfter(rangeEnd)) {
                    throw new IllegalArgumentException("Range start time is after range end time.");
                }
                if (sort != null && sort.equals("EVENT_DATE")) {
                    if (onlyAvailable) {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsPeriodSortEventDateAvailableCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsPeriodSortEventDateAvailableCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsPeriodSortEventDateAvailableText(
                                    State.PUBLISHED.toString(),
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsPeriodSortEventDateAvailable(
                                    State.PUBLISHED.toString(),
                                    rangeStart,
                                    rangeEnd,
                                    pageable);
                        }
                    } else {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsPeriodSortEventDateCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsPeriodSortEventDateCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsPeriodSortEventDateText(
                                    State.PUBLISHED.toString(),
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsPeriodSortEventDate(
                                    State.PUBLISHED.toString(),
                                    rangeStart,
                                    rangeEnd,
                                    pageable);
                        }
                    }
                } else if (sort != null && sort.equals("VIEWS")) {
                    if (onlyAvailable) {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsPeriodSortViewsAvailableCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsPeriodSortViewsAvailableCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsPeriodSortViewsAvailableText(
                                    State.PUBLISHED.toString(),
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsPeriodSortViewsAvailable(
                                    State.PUBLISHED.toString(),
                                    rangeStart,
                                    rangeEnd,
                                    pageable);
                        }
                    } else {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsPeriodSortViewsCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsPeriodSortViewsCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsPeriodSortViewsText(
                                    State.PUBLISHED.toString(),
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsPeriodSortViews(
                                    State.PUBLISHED.toString(),
                                    rangeStart,
                                    rangeEnd,
                                    pageable);
                        }
                    }
                } else {
                    if (onlyAvailable) {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsPeriodAvailableCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsPeriodAvailableCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsPeriodAvailableText(
                                    State.PUBLISHED.toString(),
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);
                        } else {
                            list = eventRepository.getEventsPeriodAvailable(
                                    State.PUBLISHED.toString(),
                                    rangeStart,
                                    rangeEnd,
                                    pageable);
                        }

                    } else {
                        if (categories != null && text != null) {
                            list = eventRepository.getEventsPeriodCategoryText(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else if (text == null && categories != null) {
                            list = eventRepository.getEventsPeriodCategory(
                                    State.PUBLISHED.toString(),
                                    categories,
                                    rangeStart,
                                    rangeEnd,
                                    pageable);

                        } else if (text != null) {
                            list = eventRepository.getEventsPeriodText(
                                    State.PUBLISHED.toString(),
                                    rangeStart,
                                    rangeEnd,
                                    text,
                                    pageable);

                        } else {
                            list = eventRepository.getEventsPeriod(
                                    State.PUBLISHED.toString(),
                                    rangeStart,
                                    rangeEnd,
                                    pageable);
                        }
                    }
                }
            }
        }


        EndpointHitDto endpointHitDto = new EndpointHitDto(null,
                "main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                timeNow.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));

        try {
            statsClient.addRequest(request.getRemoteAddr(), endpointHitDto);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }

        if (list.size() == 0) {
            return new ArrayList<>();
        }

        return list.stream().map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto getEventByIdAndStatsPublic(HttpServletRequest request, Long eventId) {
        Event event = eventRepository.getEventByIdAndState(eventId, State.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("Event not found.");
        }
        String timeStart = event.getCreatedOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String[] uris = {request.getRequestURI()};

        ResponseEntity<Object> response = statsClient.getStats(request.getRequestURI(), timeStart, timeNow, uris, true);
        List<ViewStats> resp = (List<ViewStats>) response.getBody();
        if (resp.size() == 0) {
            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
        }

        EndpointHitDto endpointHitDto = new EndpointHitDto(null,
                "main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                timeNow);

        statsClient.addRequest(request.getRemoteAddr(), endpointHitDto);

        return EventMapper.toEventFullDto(event);
    }
}