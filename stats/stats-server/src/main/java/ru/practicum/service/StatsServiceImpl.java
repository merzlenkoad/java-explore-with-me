package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.statsDto.EndpointHitDto;
import ru.practicum.statsDto.ViewStats;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public EndpointHitDto addRequest(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = StatsMapper.toEndpointHit(endpointHitDto);

        return StatsMapper.toEndpointHitDto(statsRepository.save(endpointHit));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("The time of the end cannot be earlier than the time of the beginning!");
        }

        if (!unique) {
            if (uris == null) {
                return statsRepository.findAllStats(start, end);
            } else {
                return statsRepository.findStats(start, end, uris);
            }
        } else {
            if (uris == null) {
                return statsRepository.findAllUniqueStats(start, end);
            } else {
                return statsRepository.findUniqueStats(start, end, uris);
            }
        }
    }
}