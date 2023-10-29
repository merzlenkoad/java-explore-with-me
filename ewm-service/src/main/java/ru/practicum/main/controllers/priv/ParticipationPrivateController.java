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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.participation.dto.ParticipationRequestDto;
import ru.practicum.main.participation.service.ParticipationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class ParticipationPrivateController {

    private final ParticipationService participationService;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> addParticipationRequestPrivate(HttpServletRequest request,
                                                          @Positive @PathVariable(required = false) Long userId,
                                                          @Positive @RequestParam(required = false) Long eventId) {
        log.info("Post request received: add participation.");
        return new ResponseEntity<>(participationService.addParticipationRequestPrivate(userId, eventId),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getParticipationRequestPrivate(HttpServletRequest request,
                                                                        @NotNull @Positive @PathVariable Long userId) {
        log.info("Get request received: get participation.");
        return new ResponseEntity<>(participationService.getParticipationRequestPrivate(userId), HttpStatus.OK);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> updateRejectedParticipationRequestPrivate(HttpServletRequest request,
                               @NotNull @Positive @PathVariable Long userId,
                               @NotNull @Positive @PathVariable(required = true, name = "requestId") Long requestId) {
        log.info("Get request received: update participation.");
        return new ResponseEntity<>(participationService.updateRejectedParticipationRequestPrivate(userId, requestId),
                HttpStatus.OK);
    }
}