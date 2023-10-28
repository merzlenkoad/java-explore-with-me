package ru.practicum.main.controllers.priv;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.dto.NewEventDto;
import ru.practicum.main.event.dto.UpdateEventUserRequest;
import ru.practicum.main.event.service.EventService;
import ru.practicum.main.participation.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(HttpServletRequest request,
                                 @Positive @PathVariable Long userId,
                                 @NotNull @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Post request received: add event.");
        return new ResponseEntity<>(eventService.addEventPrivate(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(HttpServletRequest request,
                                                        @NotNull @Positive @PathVariable(required = false) Long userId,
                                                        @PositiveOrZero
                                                        @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @Positive
                                                        @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get request received: get events.");
        return new ResponseEntity<>(eventService.getEventsPrivate(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(HttpServletRequest request,
                                 @Positive @PathVariable(required = false) Long userId,
                                 @Positive @PathVariable(required = false) Long eventId) {
        log.info("Get request received: get event.");
        return new ResponseEntity<>(eventService.getEventPrivate(userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsEventsUser(HttpServletRequest request,
                                                               @Positive @PathVariable Long userId,
                                                               @Positive @PathVariable Long eventId) {
        log.info("Get request received: get events of user.");
        return new ResponseEntity<>(eventService.getRequestsEventsUserPrivate(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventUserRequest(HttpServletRequest request,
                                               @Positive @PathVariable(required = false) Long userId,
                                               @Positive @PathVariable(required = false) Long eventId,
                                               @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Patch request received: update event.");
        return new ResponseEntity<>(eventService.updateEventPrivate(userId, eventId, updateEventUserRequest),
                HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateEventRequestStatus(
            HttpServletRequest request,
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Patch request received: update status event.");
        return new ResponseEntity<>(eventService.updateEventRequestStatusPrivate(
                userId, eventId, eventRequestStatusUpdateRequest), HttpStatus.OK);
    }
}