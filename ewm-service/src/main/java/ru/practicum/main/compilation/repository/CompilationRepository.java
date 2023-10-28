package ru.practicum.main.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findCompilationByPinnedIs(Boolean pinned, Pageable pageable);

    Compilation findCompilationById(Long compId);

    void removeCompilationById(Long compId);
}