package ru.practicum.main.participation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.participation.model.ParticipationRequest;

import java.util.List;


public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> getParticipationRequestsByRequesterAndEvent(Long userId, Long eventId);

    List<ParticipationRequest> getParticipationRequestByIdIn(List<Long> requestId);

    List<ParticipationRequest> getParticipationRequestsByRequesterAndEventNotIn(Long userId, List<Long> eventIdList);

    ParticipationRequest getParticipationRequestByIdAndRequester(Long requestId, Long userId);

    List<ParticipationRequest> getParticipationRequestsByEvent(Long eventId);

    List<ParticipationRequest> getParticipationRequestsByRequester(Long userId);
}