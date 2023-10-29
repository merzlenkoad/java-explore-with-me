package ru.practicum.mapper;


import ru.practicum.statsDto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.constant.Constants.DATE_FORMAT;

public class StatsMapper {
    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .build();
    }

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .build();
    }
}
