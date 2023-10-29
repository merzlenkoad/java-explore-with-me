package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statsDto.ViewStats;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;


public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {

    @Query(value = "select new ru.practicum.statsDto.ViewStats(eh.app, eh.uri, cast(count(eh.ip) " +
            "AS int) as hits) " +
            "from EndpointHit as eh " +
            "where eh.uri in ?3 " +
            "and eh.timestamp >= ?1 " +
            "and eh.timestamp <=?2 " +
            "group by eh.app, eh.uri order by hits desc")
    List<ViewStats> findStats(LocalDateTime startTime, LocalDateTime endTime, String[] uris);

    @Query(value = "select new ru.practicum.statsDto.ViewStats(eh.app, eh.uri, cast(count(eh.ip) " +
            "AS int) as hits) " +
            "from EndpointHit as eh " +
            "where eh.timestamp >= ?1 " +
            "and eh.timestamp <=?2 " +
            "group by eh.app, eh.uri order by hits desc")
    List<ViewStats> findAllStats(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "select new ru.practicum.statsDto.ViewStats(eh.app, eh.uri, cast(count(DISTINCT eh.ip) " +
            "AS int) as hits) " +
            "from EndpointHit as eh " +
            "where eh.uri in ?3 " +
            "and eh.timestamp >= ?1 " +
            "and eh.timestamp <=?2 " +
            "group by eh.app, eh.uri order by hits desc")
    List<ViewStats> findUniqueStats(LocalDateTime startTime, LocalDateTime endTime, String[] uris);

    @Query(value = "select new ru.practicum.statsDto.ViewStats(eh.app, eh.uri,cast(count(DISTINCT eh.ip) " +
            "AS int) as hits) " +
            "from EndpointHit as eh " +
            "where eh.timestamp >= ?1 " +
            "and eh.timestamp <=?2 " +
            "group by eh.app, eh.uri order by hits desc")
    List<ViewStats> findAllUniqueStats(LocalDateTime startTime, LocalDateTime endTime);
}
