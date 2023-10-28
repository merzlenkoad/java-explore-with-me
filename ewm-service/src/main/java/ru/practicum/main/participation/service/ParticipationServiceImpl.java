package ru.practicum.main.participation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.State;
import ru.practicum.main.event.model.Status;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.OverflowLimitException;
import ru.practicum.main.exception.DuplicateParticipationException;
import ru.practicum.main.participation.mapper.ParticipationMapper;
import ru.practicum.main.participation.dto.ParticipationRequestDto;
import ru.practicum.main.participation.model.ParticipationRequest;
import ru.practicum.main.participation.repository.ParticipationRepository;
import ru.practicum.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {
    private final ParticipationRepository participationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public List<ParticipationRequestDto> getParticipationRequestPrivate(Long userId) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("User not found.");
        }
        List<Long> eventIds = eventRepository.getEventsByInitiatorId(userId)
                .stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        List<ParticipationRequest> list;
        if (eventIds.size() == 0) {
            list = participationRepository.getParticipationRequestsByRequester(userId);
        } else {
            list = participationRepository.getParticipationRequestsByRequesterAndEventNotIn(userId, eventIds);
        }
        return list.stream().map(ParticipationMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto addParticipationRequestPrivate(Long userId, Long eventId) {
        Event event = eventRepository.getEventsById(eventId);
        List<ParticipationRequest> participationRequestList = participationRepository.getParticipationRequestsByRequesterAndEvent(userId, eventId);

        validateAddParticipationRequestPrivate(event, participationRequestList, userId);

        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(eventId)
                .requester(userId)
                .build();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            participationRequest.setStatus(Status.PENDING);
            eventRepository.save(event);
        }

        ParticipationRequest newParticipationRequest = participationRepository.save(participationRequest);
        ParticipationRequestDto participationRequestDto = ParticipationMapper
                .toParticipationRequestDto(newParticipationRequest);
        participationRequestDto.setId(newParticipationRequest.getId());


        return participationRequestDto;
    }

    @Transactional
    @Override
    public ParticipationRequestDto updateRejectedParticipationRequestPrivate(Long userId, Long requestId) {

        ParticipationRequest participationRequest = participationRepository.getParticipationRequestByIdAndRequester(requestId, userId);
        if (participationRequest == null) {
            throw new NotFoundException("participation not found.");
        }
        if (participationRequest.getStatus().equals(Status.PENDING)) {
            participationRequest.setStatus(Status.CANCELED);
        } else if (participationRequest.getStatus().equals(Status.CONFIRMED)) {
            Event event = eventRepository.getEventsById(participationRequest.getEvent());
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
            participationRequest.setStatus(Status.CANCELED);
        }

        return ParticipationMapper
                .toParticipationRequestDto(participationRepository.save(participationRequest));
    }

    private void validateAddParticipationRequestPrivate(Event event, List<ParticipationRequest> participationRequestList, Long userId) {
        if (participationRequestList.size() != 0) {
            throw new DuplicateParticipationException("Duplicate Participation.");
        }
        if (event == null) {
            throw new IllegalArgumentException("Event not found.");
        } else if (event.getInitiator().getId().equals(userId)) {
            throw new DuplicateParticipationException("Requesting of own event");
        } else if (event.getState() == null || !event.getState().equals(State.PUBLISHED)) {
            throw new DuplicateParticipationException("Wrong status.");
        } else if (event.getConfirmedRequests() != null
                && event.getParticipantLimit() > 0
                && event.getConfirmedRequests().equals(Long.valueOf(event.getParticipantLimit()))) {
            throw new OverflowLimitException("The limit of Participant");
        }
    }
}