package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.statsDto.EndpointHitDto;
import ru.practicum.statsDto.ViewStats;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constant.Constants.DATE_FORMAT;

@Slf4j
@RestController
public class StatsServerController {

    private final StatsService statsService;

    @Autowired
    public StatsServerController(StatsService statsService) {
        this.statsService = statsService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public EndpointHitDto addUser(
            @RequestBody EndpointHitDto endpointHitDto) {
        log.info("POST request received: saving information that there was a request to the endpoint.");
        return statsService.addRequest(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
                                    @RequestParam(name = "uris", required = false) String[] uris,
                                    @RequestParam(name = "unique", defaultValue = "false")
                                        boolean unique) {
        log.info("GET request received: getting statistics of visits.");
        return statsService.getStats(start, end, uris, unique);
    }
}
