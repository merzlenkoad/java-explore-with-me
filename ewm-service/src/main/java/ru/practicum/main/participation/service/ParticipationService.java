package ru.practicum.main.participation.service;


import ru.practicum.main.participation.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {

    List<ParticipationRequestDto> getParticipationRequestPrivate(Long userId);

    ParticipationRequestDto addParticipationRequestPrivate(Long userId, Long eventId);

    ParticipationRequestDto updateRejectedParticipationRequestPrivate(Long userId, Long requestId);

}