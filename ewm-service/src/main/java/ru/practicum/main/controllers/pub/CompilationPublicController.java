package ru.practicum.main.controllers.pub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationPublicController {

    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilationsPublic(HttpServletRequest request,
                                                                     @RequestParam(required = false, name = "pinned") Boolean pinned,
                                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get request received: get compilations.");
        return new ResponseEntity<>(compilationService.getCompilationsPublic(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationByIdPublic(HttpServletRequest request,
                                                   @Positive @PathVariable Long compId) {
        log.info("Get request received: get compilations by id.");
        return new ResponseEntity<>(compilationService.getCompilationByIdPublic(compId), HttpStatus.OK);
    }
}