package ru.practicum.main.compilation.service;

import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilationsPublic(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationByIdPublic(Long compId);

    CompilationDto addCompilationAdmin(NewCompilationDto newCompilationDto);

    void deleteCompilationByIdAdmin(Long compId);

    CompilationDto updateCompilationByIdAdmin(Long compId, UpdateCompilationRequest updateCompilationRequest);

}