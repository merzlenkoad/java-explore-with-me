package ru.practicum.main.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationRequest;
import ru.practicum.main.compilation.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto>  addCompilationAdmin(HttpServletRequest request,
                                                                @Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Post request received: adding a new compilation.");
        return new ResponseEntity<>(compilationService.addCompilationAdmin(newCompilationDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilationByIdAdmin(HttpServletRequest request,
                                                                     @Positive @PathVariable Long compId,
                                                                     @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Patch request received: update compilation.");
        return new ResponseEntity<>(compilationService.updateCompilationByIdAdmin(compId, updateCompilationRequest),
                HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{compId}")
    public void deleteCompilationByIdAdmin(HttpServletRequest request,
                                           @Positive @PathVariable("compId") Long compId) {
        log.info("Delete request received: delete compilation.");
        compilationService.deleteCompilationByIdAdmin(compId);
    }
}