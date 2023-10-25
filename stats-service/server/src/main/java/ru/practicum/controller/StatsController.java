package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static ru.practicum.constant.Constants.DATE_FORMAT;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<?> addStatsRecord(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("POST request received: saving information that there was a request to the endpoint.");
        return new ResponseEntity<>(statsService.addStats(endpointHitDto), CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(@RequestParam @DateTimeFormat(pattern = DATE_FORMAT)
                                                       LocalDateTime start,
                                                       @RequestParam @DateTimeFormat(pattern = DATE_FORMAT)
                                                       LocalDateTime end,
                                                       @RequestParam(name = "uris", required = false) List<String> uris,
                                                       @RequestParam(name = "unique", defaultValue = "false")
                                                       boolean unique) {
        log.info("GET request received: getting statistics of visits.");
        return new ResponseEntity<>(statsService.getStats(start, end, uris, unique), OK);
    }
}