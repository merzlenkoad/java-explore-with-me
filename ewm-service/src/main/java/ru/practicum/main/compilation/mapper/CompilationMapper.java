package ru.practicum.main.compilation.mapper;

import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationRequest;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList()))
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static Compilation toCompilation(UpdateCompilationRequest updateCompilationRequest, Set<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(updateCompilationRequest.isPinned())
                .build();
    }
}