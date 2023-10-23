package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsRepository;
import ru.practicum.EndpointHitDto;

import ru.practicum.ViewStatsDto;

import javax.validation.ValidationException;
import java.util.List;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsMapper statsMapper;
    private final StatsRepository statsRepository;

    @Override
    public String addStats(EndpointHitDto endpointHitDto) {
        Stats statsRecord = statsMapper.toStats(endpointHitDto);
        return statsRepository.save(statsRecord).toString();
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (!end.isBefore(start)) {
            if (unique) {
                if (uris == null) {
                    return statsRepository.findStatsByUniqueWithoutUris(start, end);
                }
                return statsRepository.findStatsByUniqueWithUris(start, end, uris);
            } else {
                if (uris == null) {
                    return statsRepository.findStatsWithoutUnique(start, end);
                }
                return statsRepository.findStats(start, end, uris);
            }
        } else {
            throw new ValidationException("The end time cannot be earlier than the start time!");
        }
    }
}