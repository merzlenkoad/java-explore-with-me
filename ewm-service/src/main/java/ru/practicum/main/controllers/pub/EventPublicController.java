package ru.practicum.main.controllers.pub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.constant.Constants.DATE_FORMAT;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class EventPublicController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsAndStatsPublic(HttpServletRequest request,
                                                                      @RequestParam(required = false) String text,
                                                                      @RequestParam(required = false) List<Long> categories,
                                                                      @RequestParam(required = false) Boolean paid,
                                                                      @DateTimeFormat(pattern = DATE_FORMAT)
                                                                      @RequestParam(required = false) LocalDateTime rangeStart,
                                                                      @DateTimeFormat(pattern = DATE_FORMAT)
                                                                      @RequestParam(required = false) LocalDateTime rangeEnd,
                                                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                                      @RequestParam(required = false) String sort,
                                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get request received: get events and stats.");
        return new ResponseEntity<>(eventService.getEventsAndStatsPublic(request, text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size), HttpStatus.OK);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<EventFullDto> getEventByIdAndStatsPublic(HttpServletRequest request,
                                                   @Positive @PathVariable("Id") Long eventId) {
        log.info("Get request received: get events and stats by id.");
        return new ResponseEntity<>(eventService.getEventByIdAndStatsPublic(request, eventId), HttpStatus.OK);
    }
}